use crate::year_2025::day_010::computation_output::ComputationOutput;
use crate::year_2025::day_010::restriction::Restriction;
use regex::Regex;
use std::{io, sync::LazyLock};

#[derive(Debug)]
pub struct FactoryMachine {
    pub light_diagram: Vec<bool>,
    pub button_wiring_schematics: Vec<Vec<usize>>,
    pub joltage_requirements: Vec<usize>,
}

static RE: LazyLock<Regex> = LazyLock::new(|| {
    Regex::new(r"^(\[[.#]+\]) ((?:\(\d+(?:,\d+)*\)\s*)+) (\{\d+(?:,\d+)*\})$").unwrap()
});
static INNER_RE: LazyLock<Regex> = LazyLock::new(|| Regex::new(r"[0-9]+").unwrap());

impl FactoryMachine {
    pub fn new(s: &str) -> io::Result<FactoryMachine> {
        let caps = RE.captures(s).unwrap();

        let mut light_diagram: Vec<bool> = Vec::new();
        for (i, c) in caps[1].chars().enumerate() {
            if i > 0 && i + 1 < caps[1].len() {
                light_diagram.push(c == '#');
            }
        }

        let mut button_wiring_schematics: Vec<Vec<usize>> = Vec::new();
        for button_wiring_raw in caps[2].split(" ") {
            let mut button_wiring: Vec<usize> = Vec::new();
            for n_raw in INNER_RE.find_iter(button_wiring_raw) {
                let n = n_raw.as_str().parse::<usize>().map_err(|_| {
                    io::Error::new(io::ErrorKind::InvalidData, "cannot parse to usize")
                })?;
                button_wiring.push(n);
            }
            button_wiring_schematics.push(button_wiring);
        }

        let mut joltage_requirements: Vec<usize> = Vec::new();
        for n_raw in INNER_RE.find_iter(&caps[3]) {
            let n = n_raw
                .as_str()
                .parse::<usize>()
                .map_err(|_| io::Error::new(io::ErrorKind::InvalidData, "cannot parse to usize"))?;
            joltage_requirements.push(n);
        }

        Ok(FactoryMachine {
            light_diagram,
            button_wiring_schematics,
            joltage_requirements,
        })
    }

    pub fn try_combination(&self, combination: &[usize]) -> bool {
        let mut light_diagram = vec![false; self.light_diagram.len()];
        combination.iter().for_each(|&index| {
            for &switch in self.button_wiring_schematics[index].iter() {
                light_diagram[switch] = !light_diagram[switch]
            }
        });

        light_diagram
            .iter()
            .zip(self.light_diagram.iter())
            .all(|(a, b)| *a == *b)
    }

    pub fn fewer_for_joltage(&self) -> Vec<usize> {
        match Self::fewer_for_joltage_internal(
            self,
            self.create_restrictions(),
            vec![None; self.button_wiring_schematics.len()],
        ) {
            ComputationOutput::Computed(result) => result,
            ComputationOutput::NotComputed => panic!("fewer_for_joltage_internal not computed"),
            ComputationOutput::Error(s) => panic!("{}", s),
        }
    }

    fn create_restrictions(&self) -> Vec<Restriction> {
        self.joltage_requirements
            .clone()
            .into_iter()
            .enumerate()
            .map(|(index_joltage, joltage_requirement)| {
                let variables = self
                    .button_wiring_schematics
                    .clone()
                    .into_iter()
                    .enumerate()
                    .filter(|(_, button)| button.contains(&index_joltage))
                    .map(|(index_button, _)| index_button)
                    .collect::<Vec<usize>>();

                let result = joltage_requirement;

                Restriction::new(variables, result)
            })
            .collect()
    }

    fn fewer_for_joltage_internal(
        &self,
        restrictions: Vec<Restriction>,
        mut result: Vec<Option<usize>>,
    ) -> ComputationOutput<Vec<usize>> {
        //Simplify the restrictions and return early if inconsistency is found
        let restrictions = match Self::simplify_restrictions(restrictions) {
            ComputationOutput::Computed(simplified_restrictions) => simplified_restrictions
                .into_iter()
                .flat_map(|restriction| match restriction.variables.first() {
                    // If we find a restriction that has only one variable, we assign it to the
                    // result
                    Some(head) if restriction.variables.len() == 1 => {
                        result[*head] = Some(restriction.result);
                        None
                    }
                    _ => Some(restriction),
                })
                .collect::<Vec<Restriction>>(),
            ComputationOutput::NotComputed => {
                return ComputationOutput::from(
                    "Funtion simplify_restrictions not computed".to_string(),
                )
            }
            ComputationOutput::Error(s) => {
                return ComputationOutput::Error(format!("Error when computing {:?} : {}", self, s))
            }
        };

        // If simplifying the restrictions led us to finish the output, we return early (with two
        // possible outcomes)
        if let Some(finished_result) = result.iter().copied().collect::<Option<Vec<usize>>>() {
            if restrictions
                .into_iter()
                .all(|restriction| restriction.checks_out(&finished_result))
            {
                return ComputationOutput::Computed(finished_result);
            } else {
                return ComputationOutput::from(format!(
                    "This final_result {:?}, did not check out for this system {:?}",
                    self, finished_result
                ));
            }
        }

        // We select the button where we want to add the recursion on
        let (button_selected, max_value) = match Self::select_button(&restrictions) {
            Some((button, min_result)) => (button, min_result),
            None => {
                return ComputationOutput::Error(format!(
                    "In '{:?}' we could not select a button to make assumptions",
                    self
                ))
            }
        };

        let mut current_optimal: ComputationOutput<Vec<usize>> = ComputationOutput::NotComputed;
        for assumption in 0..=max_value {
            // We plan the recursion for this assumption
            let mut restrictions_with_assumption = restrictions.clone();
            restrictions_with_assumption.push(Restriction {
                variables: vec![button_selected],
                result: assumption,
            });

            // Compute the recursion
            let new_optimal = match (
                &current_optimal,
                self.fewer_for_joltage_internal(
                    restrictions_with_assumption.clone(),
                    result.clone(),
                ),
            ) {
                (
                    ComputationOutput::Computed(current_optimal_defined),
                    ComputationOutput::Computed(partial_result),
                ) if partial_result.iter().sum::<usize>()
                    < current_optimal_defined.iter().sum::<usize>() =>
                {
                    // We found a new best
                    ComputationOutput::Computed(partial_result)
                }

                (ComputationOutput::NotComputed, ComputationOutput::Computed(partial_result)) => {
                    // We found the first best
                    ComputationOutput::Computed(partial_result)
                }

                _ => ComputationOutput::NotComputed,
            };

            if let ComputationOutput::Computed(_) = &new_optimal {
                current_optimal = new_optimal;
            }
        }

        current_optimal
    }

    fn simplify_restrictions(
        mut restrictions: Vec<Restriction>,
    ) -> ComputationOutput<Vec<Restriction>> {
        let mut change: Option<(usize, Restriction)> = None;
        loop {
            for (i, restriction_i) in restrictions.iter().enumerate() {
                for restriction_j in restrictions.iter() {
                    if *restriction_i != *restriction_j {
                        match restriction_i.minus(restriction_j) {
                            ComputationOutput::Computed(new_restriction_i) => {
                                change = Some((i, new_restriction_i));
                                break;
                            }
                            ComputationOutput::Error(s) => return ComputationOutput::from(s),
                            _ => (),
                        }
                    }
                }
            }
            match change {
                Some((i, new_restriction_i)) => {
                    restrictions[i] = new_restriction_i;
                    change = None;
                }
                None => break,
            }
        }

        ComputationOutput::Computed(restrictions)
    }

    fn select_button(restrictions: &[Restriction]) -> Option<(usize, usize)> {
        let min_result = restrictions
            .iter()
            .map(|restriction| restriction.result)
            .min()?;

        let button = restrictions.iter().find_map(|restriction| {
            if restriction.result == min_result {
                restriction.variables.first()
            } else {
                None
            }
        })?;

        Some((*button, min_result))
    }
}

use crate::year_2025::day_010::computation_output::ComputationOutput;

#[derive(Debug, PartialEq, Clone)]
pub struct Restriction {
    pub variables: Vec<usize>,
    pub result: usize,
}

impl Restriction {
    pub fn new(variables: Vec<usize>, result: usize) -> Self {
        Restriction { variables, result }
    }

    // We try to simplify this restriction if found another that fits inside
    // This method can be called with restrictions of 1 variable
    pub fn minus(&self, other: &Restriction) -> ComputationOutput<Restriction> {
        if other
            .variables
            .iter()
            .all(|other_variable| self.variables.contains(other_variable))
        {
            let variables = self
                .variables
                .clone()
                .into_iter()
                .filter(|self_variable| !other.variables.contains(self_variable))
                .collect();

            match self.result.checked_sub(other.result) {
                Some(result) => ComputationOutput::Computed(Restriction { variables, result }),
                None => ComputationOutput::Error(format!(
                    "Canot perform minus from '{:?}' to '{:?}'",
                    self, other
                )),
            }
        } else {
            ComputationOutput::NotComputed
        }
    }

    pub fn checks_out(&self, result: &[usize]) -> bool {
        self.variables
            .iter()
            .map(|&variable| result[variable])
            .sum::<usize>()
            == self.result
    }
}

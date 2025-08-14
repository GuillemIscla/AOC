use super::element::Element;
use regex::Regex;
use std::io::{self, Error, ErrorKind};

pub struct Laboratory {
    pub replacements: Vec<(Element, Vec<Element>)>,
    pub target_molecule: Vec<Element>,
}

impl Laboratory {
    pub fn new(replacements_raw: Vec<String>, target_molecule_raw: String) -> Laboratory {
        let mut replacements = Vec::new();

        replacements_raw.iter().for_each(|replacement_raw| {
            add_replacement_raw(&mut replacements, replacement_raw)
                .unwrap_or_else(|_| panic!("Could not parse replacement_raw '{}'", replacement_raw))
        });

        let target_molecule = Element::from_raw_to_vec(&target_molecule_raw);

        Laboratory {
            replacements,
            target_molecule,
        }
    }

    pub fn process_replacements(&self, molecule: &[Element]) -> Vec<Vec<Element>> {
        let mut result: Vec<Vec<Element>> = Vec::new();
        self.replacements.iter().for_each(|(key, value)| {
            let matches = find_matches(molecule, key);
            matches.into_iter().for_each(|one_match| {
                let mut new_molecule = Vec::with_capacity(molecule.len() - 1 + value.len());
                new_molecule.extend_from_slice(&molecule[..one_match]);
                new_molecule.extend_from_slice(value);
                new_molecule.extend_from_slice(&molecule[one_match + 1..]);
                result.push(new_molecule);
            });
        });

        result.sort();
        result.dedup();
        result
    }

    pub fn formula_to_reduce(&self) -> isize {
        let total = self.target_molecule.len() as isize;
        let rn_and_ar = self
            .target_molecule
            .iter()
            .filter(|element| element.raw == "Rn" || element.raw == "Ar")
            .count() as isize;
        let y = self
            .target_molecule
            .iter()
            .filter(|element| element.raw == "Y")
            .count() as isize;
        total - rn_and_ar - 2 * y - 1
    }
}

fn add_replacement_raw(
    replacements: &mut Vec<(Element, Vec<Element>)>,
    replacement_raw: &str,
) -> io::Result<()> {
    let regex_raw = "([A-z]+) => ([A-z]+)";

    let regex = Regex::new(regex_raw).map_err(|e| Error::new(ErrorKind::InvalidInput, e))?;

    let captures = regex.captures(replacement_raw).ok_or_else(|| {
        Error::new(
            ErrorKind::InvalidData,
            format!("Invalid line: {}", replacement_raw),
        )
    })?;

    let key = Element::from_raw(&captures[1]);
    let value = Element::from_raw_to_vec(&captures[2]);
    replacements.push((key, value));
    Ok(())
}

fn find_matches(vec: &[Element], pattern: &Element) -> Vec<usize> {
    vec.iter()
        .enumerate()
        .filter_map(|(i, e)| if e == pattern { Some(i) } else { None })
        .collect()
}

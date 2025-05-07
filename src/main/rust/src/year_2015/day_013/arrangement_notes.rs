use itertools::Itertools;
use regex::Regex;
use std::collections::HashMap;
use std::io::{self, Error, ErrorKind};

#[derive(Debug)]
pub struct ArrangementNotes {
    all_persons: Vec<String>,
    all_happiness: HashMap<(String, String), isize>,
}

impl ArrangementNotes {
    pub fn from_raw(input: Vec<&str>) -> io::Result<ArrangementNotes> {
        let mut all_happiness: HashMap<(String, String), isize> = HashMap::new();

        let all_lines_parsed = input
            .iter()
            .map(|line| Self::parse_line(line))
            .collect::<io::Result<Vec<(String, String, isize)>>>()?;

        let all_persons = all_lines_parsed
            .into_iter()
            .flat_map(|(person_1, person_2, points)| {
                all_happiness.insert((person_1.clone(), person_2.clone()), points);
                vec![person_1, person_2]
            })
            .collect::<Vec<String>>()
            .into_iter()
            .unique()
            .collect::<Vec<String>>();

        Ok(ArrangementNotes {
            all_persons,
            all_happiness,
        })
    }

    fn parse_line(line: &str) -> io::Result<(String, String, isize)> {
        let regex_raw =
            "([A-z]+) would (gain|lose) ([0-9]+) happiness units by sitting next to ([A-z]+).";
        let regex = Regex::new(regex_raw).map_err(|e| Error::new(ErrorKind::InvalidInput, e))?;

        let captures = regex
            .captures(line)
            .ok_or_else(|| Error::new(ErrorKind::InvalidData, format!("Invalid line: {}", line)))?;

        let person_1 = String::from(&captures[1]);
        let gain_modifier = if &captures[2] == "gain" { 1 } else { -1 };
        let points = &captures[3]
            .parse::<isize>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;
        let person_2 = String::from(&captures[4]);

        Ok((person_1, person_2, gain_modifier * points))
    }

    pub fn person_count(&self) -> usize {
        self.all_persons.len()
    }

    pub fn permute_and_evaluate(&self, permutation: Vec<usize>) -> isize {
        (0..permutation.len())
            .map(|index| {
                let before = if index == 0 {
                    permutation.len().wrapping_sub(1)
                } else {
                    index.wrapping_sub(1)
                };
                let after = (index + 1) % permutation.len();

                self.happiness_by_position(permutation[index], permutation[before])
                    + self.happiness_by_position(permutation[index], permutation[after])
            })
            .sum()
    }

    fn happiness_by_position(&self, position_1: usize, position_2: usize) -> isize {
        *self
            .all_happiness
            .get(&(
                self.all_persons[position_1].clone(),
                self.all_persons[position_2].clone(),
            ))
            .unwrap()
    }

    pub fn add_ambivalent(&mut self, name: String) {
        self.all_persons.iter().for_each(|p| {
            self.all_happiness.insert((p.clone(), name.clone()), 0);
            self.all_happiness.insert((name.clone(), p.clone()), 0);
        });
        self.all_persons.push(name);
    }
}

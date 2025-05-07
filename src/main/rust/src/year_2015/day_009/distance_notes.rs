use itertools::Itertools;
use regex::Regex;
use std::collections::HashMap;
use std::io::{self, Error, ErrorKind};

#[derive(Debug)]
pub struct DistanceNotes {
    all_cities: Vec<String>,
    all_distances: HashMap<(String, String), usize>,
}

impl DistanceNotes {
    pub fn from_raw(input: Vec<&str>) -> io::Result<DistanceNotes> {
        let mut all_distances: HashMap<(String, String), usize> = HashMap::new();

        let all_lines_parsed = input
            .iter()
            .map(|line| Self::parse_line(line))
            .collect::<io::Result<Vec<(String, String, usize)>>>()?;

        let all_cities = all_lines_parsed
            .into_iter()
            .flat_map(|(from, to, distance)| {
                all_distances.insert((from.clone(), to.clone()), distance);
                all_distances.insert((to.clone(), from.clone()), distance);
                vec![from, to]
            })
            .collect::<Vec<String>>()
            .into_iter()
            .unique()
            .collect::<Vec<String>>();

        Ok(DistanceNotes {
            all_cities,
            all_distances,
        })
    }

    fn parse_line(line: &str) -> io::Result<(String, String, usize)> {
        let regex_raw = "([A-z]+) to ([A-z]+) = ([0-9]+)";
        let regex = Regex::new(regex_raw).map_err(|e| Error::new(ErrorKind::InvalidInput, e))?;

        let captures = regex
            .captures(line)
            .ok_or_else(|| Error::new(ErrorKind::InvalidData, format!("Invalid line: {}", line)))?;

        let from = String::from(&captures[1]);
        let to = String::from(&captures[2]);
        let distance = &captures[3]
            .parse::<usize>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;

        Ok((from, to, *distance))
    }

    pub fn city_count(&self) -> usize {
        self.all_cities.len()
    }

    pub fn permute_and_travel(&self, permutation: Vec<usize>) -> usize {
        (0..permutation.len() - 1)
            .map(|index| {
                self.all_distances
                    .get(&(
                        self.all_cities[permutation[index]].clone(),
                        self.all_cities[permutation[index + 1]].clone(),
                    ))
                    .unwrap()
            })
            .sum()
    }
}

use crate::year_2025::day_012::present::Present;
use regex::Regex;
use std::io;
use std::sync::LazyLock;

#[derive(Debug)]
pub struct TreeArea {
    width: usize,
    height: usize,
    presents: Vec<usize>,
}

static RE: LazyLock<Regex> = LazyLock::new(|| Regex::new(r"([0-9]+)x([0-9]+): ([0-9 ]+)").unwrap());

impl TreeArea {
    pub fn new(raw: &str) -> io::Result<Self> {
        let captures = RE.captures(raw).ok_or_else(|| {
            io::Error::new(io::ErrorKind::InvalidData, format!("Invalid line: {}", raw))
        })?;

        let width = captures[1]
            .parse::<usize>()
            .map_err(|e| io::Error::new(io::ErrorKind::InvalidData, e))?;

        let height = captures[2]
            .parse::<usize>()
            .map_err(|e| io::Error::new(io::ErrorKind::InvalidData, e))?;

        let presents = captures[3]
            .split(" ")
            .map(|present| {
                present
                    .parse::<usize>()
                    .map_err(|e| io::Error::new(io::ErrorKind::InvalidData, e))
            })
            .collect::<io::Result<Vec<usize>>>()?;

        Ok(TreeArea {
            width,
            height,
            presents,
        })
    }

    pub fn it_surely_fits(&self, presents: &[Present]) -> bool {
        let witdh_reduced = self.width.checked_sub(self.width % 3).unwrap();
        let height_reduced = self.height.checked_sub(self.height % 3).unwrap();

        witdh_reduced * height_reduced
            >= self
                .presents
                .iter()
                .enumerate()
                .map(|(present_index, &present_count)| {
                    presents[present_index].total_size * present_count
                })
                .sum::<usize>()
    }

    pub fn it_surely_does_not_fit(&self, presents: &[Present]) -> bool {
        self.width * self.height
            < self
                .presents
                .iter()
                .enumerate()
                .map(|(present_index, &present_count)| {
                    presents[present_index].small_size * present_count
                })
                .sum::<usize>()
    }
}

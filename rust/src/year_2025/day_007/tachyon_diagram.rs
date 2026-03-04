use std::collections::HashMap;
use std::fmt;
use std::io;

#[derive(PartialEq)]
pub enum TachyonCell {
    Empty,
    Splitter,
    Beam,
    Start,
}

pub struct TachyonDiagram {
    cells: Vec<Vec<TachyonCell>>,
}

pub struct TachyonBeamResult {
    pub splits: u32,
    pub timeline_counters: HashMap<usize, u64>,
}

impl TachyonDiagram {
    pub fn new(s: String) -> io::Result<Self> {
        let cells = s
            .split("\n")
            .map(|line| {
                line.chars()
                    .map(TachyonCell::new)
                    .collect::<io::Result<Vec<TachyonCell>>>()
            })
            .collect::<io::Result<Vec<Vec<TachyonCell>>>>()?;

        Ok(TachyonDiagram { cells })
    }

    pub fn shoot_beam(&mut self) -> TachyonBeamResult {
        let mut splits = 0;
        let mut timeline_counters: HashMap<usize, u64> = HashMap::new();
        for i in 1..self.cells.len() {
            let mut new_timelines_counters: HashMap<usize, u64> = HashMap::new();
            for j in 0..self.cells[i].len() {
                if self.cells[i][j] == TachyonCell::Splitter {
                    if self.cells[i - 1][j] == TachyonCell::Beam {
                        self.cells[i][j - 1] = TachyonCell::Beam;
                        self.cells[i][j + 1] = TachyonCell::Beam;
                        splits += 1;

                        let old_timeline = timeline_counters.get(&j).copied().unwrap_or(0);
                        *new_timelines_counters.entry(j - 1).or_insert(0) += old_timeline;
                        *new_timelines_counters.entry(j + 1).or_insert(0) += old_timeline;
                    }
                } else if self.cells[i - 1][j] == TachyonCell::Beam {
                    self.cells[i][j] = TachyonCell::Beam;
                    let old_timeline = timeline_counters.get(&j).copied().unwrap_or(0);
                    *new_timelines_counters.entry(j).or_insert(0) += old_timeline;
                } else if self.cells[i - 1][j] == TachyonCell::Start {
                    self.cells[i][j] = TachyonCell::Beam;
                    *new_timelines_counters.entry(j).or_insert(0) += 1;
                }
            }

            timeline_counters = new_timelines_counters;
        }

        TachyonBeamResult {
            splits,
            timeline_counters,
        }
    }
}

impl fmt::Display for TachyonDiagram {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        for row in &self.cells {
            for cell in row {
                write!(f, "{}", cell)?;
            }
            writeln!(f)?; // newline after each row
        }
        Ok(())
    }
}

impl TachyonCell {
    pub fn new(c: char) -> io::Result<Self> {
        match c {
            '.' => Ok(TachyonCell::Empty),
            'S' => Ok(TachyonCell::Start),
            '^' => Ok(TachyonCell::Splitter),
            '|' => Ok(TachyonCell::Beam),
            _ => Err(io::Error::new(
                io::ErrorKind::InvalidInput,
                format!("'{}' is not valid for a cell in the TachyonDiagram", c),
            )),
        }
    }
}

impl fmt::Display for TachyonCell {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            TachyonCell::Empty => write!(f, "."),
            TachyonCell::Start => write!(f, "S"),
            TachyonCell::Splitter => write!(f, "^"),
            TachyonCell::Beam => write!(f, "|"),
        }
    }
}

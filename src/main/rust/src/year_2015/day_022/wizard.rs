use regex::Regex;
use std::fmt::Display;
use std::io::{self, Error, ErrorKind};

#[derive(Debug, Clone)]
pub struct Wizard {
    pub hit_points: isize,
    pub mana_points: usize,
    pub armor: usize,
}

impl Wizard {
    pub fn new(hit_points: isize, mana_points: usize) -> Wizard {
        Wizard {
            hit_points,
            mana_points,
            armor: 0,
        }
    }

    pub fn from_raw(raw_lines: Vec<String>) -> io::Result<Wizard> {
        let regex_raw = r"[^\d]+([\d]+)";
        match &raw_lines[..] {
            [hit_points_raw, mana_points_raw] => {
                let regex =
                    Regex::new(regex_raw).map_err(|e| Error::new(ErrorKind::InvalidInput, e))?;

                let captures_hit_points = regex.captures(hit_points_raw).ok_or_else(|| {
                    Error::new(
                        ErrorKind::InvalidData,
                        format!("Invalid line: {}", hit_points_raw),
                    )
                })?;

                let captures_mana_points = regex.captures(mana_points_raw).ok_or_else(|| {
                    Error::new(
                        ErrorKind::InvalidData,
                        format!("Invalid line: {}", mana_points_raw),
                    )
                })?;

                let hit_points = captures_hit_points[1]
                    .parse::<isize>()
                    .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;

                let mana_points = captures_mana_points[1]
                    .parse::<usize>()
                    .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;

                Ok(Wizard::new(hit_points, mana_points))
            }
            _ => Err(Error::new(
                ErrorKind::InvalidData,
                format!(
                    "Wrong number of lines to parse a character. {:?}",
                    raw_lines
                ),
            )),
        }
    }
}

impl Display for Wizard {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> Result<(), std::fmt::Error> {
        write!(f, "Wizard({}, {})", self.hit_points, self.mana_points)
    }
}

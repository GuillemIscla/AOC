use regex::Regex;
use std::fmt::Display;
use std::io::{self, Error, ErrorKind};

#[derive(Debug, Clone)]
pub struct Warrior {
    pub hit_points: isize,
    pub damage: usize,
}

impl Warrior {
    pub fn new(hit_points: isize, damage: usize) -> Warrior {
        Warrior { hit_points, damage }
    }

    pub fn from_raw(raw_lines: Vec<String>) -> io::Result<Warrior> {
        let regex_raw = r"[^\d]+([\d]+)";
        match &raw_lines[..] {
            [hit_points_raw, damage_raw] => {
                let regex =
                    Regex::new(regex_raw).map_err(|e| Error::new(ErrorKind::InvalidInput, e))?;

                let captures_hit_points = regex.captures(hit_points_raw).ok_or_else(|| {
                    Error::new(
                        ErrorKind::InvalidData,
                        format!("Invalid line: {}", hit_points_raw),
                    )
                })?;
                let captures_damage = regex.captures(damage_raw).ok_or_else(|| {
                    Error::new(
                        ErrorKind::InvalidData,
                        format!("Invalid line: {}", damage_raw),
                    )
                })?;

                let hit_points = captures_hit_points[1]
                    .parse::<isize>()
                    .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;

                let damage = captures_damage[1]
                    .parse::<usize>()
                    .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;

                Ok(Warrior::new(hit_points, damage))
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

impl Display for Warrior {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> Result<(), std::fmt::Error> {
        write!(f, "Warrior({}, {})", self.hit_points, self.damage)
    }
}

use crate::year_2015::day_007::circuit_part_input::CircuitPartInput;
use regex::Regex;
use std::fmt;
use std::fmt::Display;
use std::io;
use std::io::{Error, ErrorKind};

pub struct CircuitPart {
    pub circuit_part_input: CircuitPartInput,
    pub output: String,
}
impl Display for CircuitPart {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(f, "{} -> {}", self.circuit_part_input, self.output)
    }
}

impl CircuitPart {
    pub fn from_raw(raw: &str) -> io::Result<CircuitPart> {
        let regex_raw = "(.*) -> ([a-z]+)";

        let regex = Regex::new(regex_raw).map_err(|e| Error::new(ErrorKind::InvalidInput, e))?;

        let captures = regex
            .captures(raw)
            .ok_or_else(|| Error::new(ErrorKind::InvalidData, format!("Invalid line: {}", raw)))?;

        if let Ok(inner) = CircuitPartInput::from_raw(&captures[1])
            .map_err(|e| io::Error::new(ErrorKind::InvalidData, e))
        {
            return Ok(CircuitPart {
                circuit_part_input: inner,
                output: String::from(&captures[2]),
            });
        }

        Ok(CircuitPart {
            circuit_part_input: CircuitPartInput::Wire(String::from(&captures[1])),
            output: String::from(&captures[2]),
        })
    }
}

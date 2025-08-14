use crate::year_2015::day_007::circuit::Circuit;
use std::fmt;
use std::fmt::Display;
use std::io;
use std::io::{Error, ErrorKind};

#[derive(Clone)]
pub enum BitwiseGateInput {
    SpecificValue(u16),
    Wire(String),
}

impl BitwiseGateInput {
    pub fn from_raw(raw: &str) -> io::Result<BitwiseGateInput> {
        if let Ok(inner) = raw
            .parse::<u16>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))
        {
            return Ok(BitwiseGateInput::SpecificValue(inner));
        }
        Ok(BitwiseGateInput::Wire(String::from(raw)))
    }

    pub fn get_value(&self, circuit: &mut Circuit) -> u16 {
        match self {
            BitwiseGateInput::SpecificValue(inner) => *inner,
            BitwiseGateInput::Wire(inner) => circuit.get_wire_value(inner),
        }
    }
}

impl Display for BitwiseGateInput {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            BitwiseGateInput::SpecificValue(specific_value) => write!(f, "{}", specific_value),
            BitwiseGateInput::Wire(wire) => write!(f, "{}", wire),
        }
    }
}

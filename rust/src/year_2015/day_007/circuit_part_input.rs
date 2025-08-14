use crate::year_2015::day_007::bitwise_gate;
use crate::year_2015::day_007::bitwise_gate::BitwiseGate;
use crate::year_2015::day_007::circuit::Circuit;
use std::fmt;
use std::fmt::Display;
use std::io;
use std::io::ErrorKind;

#[derive(Clone)]
pub enum CircuitPartInput {
    SpecificValue(u16),
    BitwiseGate(BitwiseGate),
    Wire(String),
}

impl Display for CircuitPartInput {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            CircuitPartInput::Wire(inner) => write!(f, "{}", inner),
            CircuitPartInput::SpecificValue(inner) => write!(f, "{}", inner),
            CircuitPartInput::BitwiseGate(bitwise_gate) => write!(f, "{}", bitwise_gate),
        }
    }
}

impl CircuitPartInput {
    pub fn from_raw(raw: &str) -> io::Result<CircuitPartInput> {
        if let Ok(inner) = raw
            .parse::<u16>()
            .map_err(|e| io::Error::new(ErrorKind::InvalidData, e))
        {
            return Ok(CircuitPartInput::SpecificValue(inner));
        }
        if let Ok(inner) = bitwise_gate::from_raw(raw) {
            return Ok(CircuitPartInput::BitwiseGate(inner));
        }
        Ok(CircuitPartInput::Wire(String::from(raw)))
    }

    pub fn get_value(&self, circuit: &mut Circuit) -> u16 {
        match self {
            CircuitPartInput::SpecificValue(specific_value) => *specific_value,
            CircuitPartInput::BitwiseGate(bitwise_gate) => bitwise_gate.process(circuit),
            CircuitPartInput::Wire(wire) => circuit.get_wire_value(wire),
        }
    }
}

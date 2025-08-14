use crate::year_2015::day_007::circuit_part::CircuitPart;
use std::collections::HashMap;
use std::fmt;
use std::fmt::Display;
use std::io;

pub struct Circuit {
    circuit_parts: Vec<CircuitPart>,
    wire_caches: HashMap<String, u16>,
}

impl Display for Circuit {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        let display_string = self
            .circuit_parts
            .iter()
            .map(|item| format!("{}", item))
            .collect::<Vec<_>>()
            .join(", ");
        write!(f, "Circuit({})", display_string)
    }
}

impl Circuit {
    pub fn from_raw(raw_input: Vec<&str>) -> io::Result<Circuit> {
        let circuit_parts = raw_input
            .into_iter()
            .map(CircuitPart::from_raw)
            .collect::<io::Result<Vec<CircuitPart>>>()?;
        Ok(Circuit {
            circuit_parts,
            wire_caches: HashMap::new(),
        })
    }

    pub fn get_wire_value(&mut self, wire: &str) -> u16 {
        match self.wire_caches.get(wire) {
            Some(value) => *value,
            None => {
                let circuit_part_input_lookup = self
                    .circuit_parts
                    .iter()
                    .find(|&circuit_part| circuit_part.output == *wire)
                    .map(|circuit_part| circuit_part.circuit_part_input.clone());

                match circuit_part_input_lookup {
                    Some(circuit_part_input) => {
                        let wire_value = circuit_part_input.get_value(self);
                        self.cache_wire_value(wire, wire_value);
                        wire_value
                    }
                    None => panic!("Not found circuit part with output '{}'", wire),
                }
            }
        }
    }

    pub fn cache_wire_value(&mut self, wire: &str, value: u16) {
        self.wire_caches.insert(String::from(wire), value);
    }

    pub fn clear_cache(&mut self) {
        self.wire_caches.clear();
    }
}

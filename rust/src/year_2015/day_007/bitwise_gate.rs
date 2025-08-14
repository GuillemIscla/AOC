use crate::year_2015::day_007::bitwise_gate_input::BitwiseGateInput;
use crate::year_2015::day_007::circuit::Circuit;
use regex::Regex;
use std::fmt;
use std::fmt::Display;
use std::io;
use std::io::{Error, ErrorKind};

#[derive(Clone)]
pub enum BitwiseGate {
    And(And),
    Or(Or),
    Lshift(Lshift),
    Rshift(Rshift),
    Not(Not),
}

impl BitwiseGate {
    pub fn process(&self, circuit: &mut Circuit) -> u16 {
        match self {
            BitwiseGate::And(and) => and.process(circuit),
            BitwiseGate::Or(or) => or.process(circuit),
            BitwiseGate::Lshift(lshift) => lshift.process(circuit),
            BitwiseGate::Rshift(rshift) => rshift.process(circuit),
            BitwiseGate::Not(not) => not.process(circuit),
        }
    }
}

impl Display for BitwiseGate {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            BitwiseGate::And(and) => write!(f, "{}", and),
            BitwiseGate::Or(or) => write!(f, "{}", or),
            BitwiseGate::Lshift(lshift) => write!(f, "{}", lshift),
            BitwiseGate::Rshift(rshift) => write!(f, "{}", rshift),
            BitwiseGate::Not(not) => write!(f, "{}", not),
        }
    }
}

#[derive(Clone)]
pub struct And {
    x: BitwiseGateInput,
    y: BitwiseGateInput,
}

impl And {
    fn process(&self, circuit: &mut Circuit) -> u16 {
        let x_bits = u16_to_bools(self.x.get_value(circuit));
        let y_bits = u16_to_bools(self.y.get_value(circuit));

        let mut r_bits = [false; 16];
        for i in 0..16 {
            r_bits[i] = x_bits[i] && y_bits[i];
        }
        bools_to_u16(r_bits)
    }

    pub fn from_raw(raw: &str) -> io::Result<And> {
        let regex_raw = "(.+) AND (.+)";
        let regex =
            Regex::new(regex_raw).map_err(|e| io::Error::new(io::ErrorKind::InvalidInput, e))?;

        let captures = regex.captures(raw).ok_or_else(|| {
            io::Error::new(io::ErrorKind::InvalidData, format!("Invalid line: {}", raw))
        })?;

        Ok(And {
            x: BitwiseGateInput::from_raw(&captures[1])?,
            y: BitwiseGateInput::from_raw(&captures[2])?,
        })
    }
}

impl Display for And {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(f, "And({}, {})", self.x, self.y)
    }
}

#[derive(Clone)]
pub struct Or {
    x: BitwiseGateInput,
    y: BitwiseGateInput,
}

impl Or {
    fn process(&self, circuit: &mut Circuit) -> u16 {
        let x_bits = u16_to_bools(self.x.get_value(circuit));
        let y_bits = u16_to_bools(self.y.get_value(circuit));

        let mut r_bits = [false; 16];
        for i in 0..16 {
            r_bits[i] = x_bits[i] || y_bits[i];
        }
        bools_to_u16(r_bits)
    }

    pub fn from_raw(raw: &str) -> io::Result<Or> {
        let regex_raw = "([a-z]+) OR ([a-z]+)";
        let regex =
            Regex::new(regex_raw).map_err(|e| io::Error::new(io::ErrorKind::InvalidInput, e))?;

        let captures = regex.captures(raw).ok_or_else(|| {
            io::Error::new(io::ErrorKind::InvalidData, format!("Invalid line: {}", raw))
        })?;

        Ok(Or {
            x: BitwiseGateInput::from_raw(&captures[1])?,
            y: BitwiseGateInput::from_raw(&captures[2])?,
        })
    }
}

impl Display for Or {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(f, "Or({}, {})", self.x, self.y)
    }
}

#[derive(Clone)]
pub struct Lshift {
    x: BitwiseGateInput,
    shift: usize,
}

impl Lshift {
    fn process(&self, circuit: &mut Circuit) -> u16 {
        let x_bits = u16_to_bools(self.x.get_value(circuit));

        let mut r_bits = [false; 16];
        let until = self.shift;
        for i in until..16 {
            r_bits[i - self.shift] = x_bits[i];
        }
        bools_to_u16(r_bits)
    }

    pub fn from_raw(raw: &str) -> io::Result<Lshift> {
        let regex_raw = "([a-z]+) LSHIFT ([0-9]+)";
        let regex =
            Regex::new(regex_raw).map_err(|e| io::Error::new(io::ErrorKind::InvalidInput, e))?;

        let captures = regex.captures(raw).ok_or_else(|| {
            io::Error::new(io::ErrorKind::InvalidData, format!("Invalid line: {}", raw))
        })?;

        Ok(Lshift {
            x: BitwiseGateInput::from_raw(&captures[1])?,
            shift: captures[2]
                .parse::<usize>()
                .map_err(|e| io::Error::new(io::ErrorKind::InvalidData, e))?,
        })
    }
}

impl Display for Lshift {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(f, "Lshift({}, {})", self.x, self.shift)
    }
}

#[derive(Clone)]
pub struct Rshift {
    x: BitwiseGateInput,
    shift: usize,
}

impl Rshift {
    fn process(&self, circuit: &mut Circuit) -> u16 {
        let x_bits = u16_to_bools(self.x.get_value(circuit));

        let mut r_bits = [false; 16];
        let until: usize = 16 - self.shift;
        for i in 0..until {
            r_bits[i + self.shift] = x_bits[i];
        }
        bools_to_u16(r_bits)
    }

    pub fn from_raw(raw: &str) -> io::Result<Rshift> {
        let regex_raw = "([a-z]+) RSHIFT ([0-9]+)";
        let regex =
            Regex::new(regex_raw).map_err(|e| io::Error::new(io::ErrorKind::InvalidInput, e))?;

        let captures = regex.captures(raw).ok_or_else(|| {
            io::Error::new(io::ErrorKind::InvalidData, format!("Invalid line: {}", raw))
        })?;

        Ok(Rshift {
            x: BitwiseGateInput::from_raw(&captures[1])?,
            shift: captures[2]
                .parse::<usize>()
                .map_err(|e| io::Error::new(io::ErrorKind::InvalidData, e))?,
        })
    }
}

impl Display for Rshift {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(f, "Rshift({}, {})", self.x, self.shift)
    }
}

#[derive(Clone)]
pub struct Not {
    x: BitwiseGateInput,
}

impl Not {
    fn process(&self, circuit: &mut Circuit) -> u16 {
        let x_bits = u16_to_bools(self.x.get_value(circuit));

        let mut r_bits = [false; 16];
        for i in 0..16 {
            r_bits[i] = !x_bits[i];
        }
        bools_to_u16(r_bits)
    }

    pub fn from_raw(raw: &str) -> io::Result<Not> {
        let regex_raw = "NOT ([a-z]+)";
        let regex =
            Regex::new(regex_raw).map_err(|e| io::Error::new(io::ErrorKind::InvalidInput, e))?;

        let captures = regex.captures(raw).ok_or_else(|| {
            io::Error::new(io::ErrorKind::InvalidData, format!("Invalid line: {}", raw))
        })?;

        Ok(Not {
            x: BitwiseGateInput::from_raw(&captures[1])?,
        })
    }
}

impl Display for Not {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(f, "Not({})", self.x)
    }
}

pub fn from_raw(raw: &str) -> io::Result<BitwiseGate> {
    if let Ok(inner) = And::from_raw(raw) {
        return Ok(BitwiseGate::And(inner));
    }
    if let Ok(inner) = Or::from_raw(raw) {
        return Ok(BitwiseGate::Or(inner));
    }
    if let Ok(inner) = Lshift::from_raw(raw) {
        return Ok(BitwiseGate::Lshift(inner));
    }
    if let Ok(inner) = Rshift::from_raw(raw) {
        return Ok(BitwiseGate::Rshift(inner));
    }
    if let Ok(inner) = Not::from_raw(raw) {
        return Ok(BitwiseGate::Not(inner));
    }

    Err(Error::new(
        ErrorKind::InvalidData,
        format!("Invalid wire input: {}", raw),
    ))
}

pub fn bools_to_u16(bits: [bool; 16]) -> u16 {
    bits.iter().fold(0u16, |acc, &bit| (acc << 1) | bit as u16)
}

pub fn u16_to_bools(n: u16) -> [bool; 16] {
    let mut bits = [false; 16];
    for i in 0..16 {
        bits[15 - i] = (n & (1 << i)) != 0;
    }
    bits
}

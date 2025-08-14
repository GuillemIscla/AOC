use crate::year_2015::day_006::light_action::LightAction;
use regex::Regex;
use std::{io, usize};

#[derive(Debug, PartialEq)]
pub struct Instruction {
    pub light_action: LightAction,
    pub x_from: usize,
    pub x_to: usize,
    pub y_from: usize,
    pub y_to: usize,
}

impl Instruction {
    pub fn from_raw(raw_instruction: &str) -> io::Result<Self> {
        let regex =
            Regex::new("(turn on|turn off|toggle) ([0-9]+),([0-9]+) through ([0-9]+),([0-9]+)")
                .map_err(|e| io::Error::new(io::ErrorKind::InvalidInput, e))?;

        let captures = regex.captures(raw_instruction).ok_or_else(|| {
            io::Error::new(
                io::ErrorKind::InvalidData,
                format!("Invalid line: {}", raw_instruction),
            )
        })?;

        let light_action = LightAction::from_raw(&captures[1])?;
        let x_from = captures[2]
            .parse::<usize>()
            .map_err(|e| io::Error::new(io::ErrorKind::InvalidData, e))?;
        let y_from = captures[3]
            .parse::<usize>()
            .map_err(|e| io::Error::new(io::ErrorKind::InvalidData, e))?;
        let x_to = captures[4]
            .parse::<usize>()
            .map_err(|e| io::Error::new(io::ErrorKind::InvalidData, e))?;
        let y_to = captures[5]
            .parse::<usize>()
            .map_err(|e| io::Error::new(io::ErrorKind::InvalidData, e))?;

        Ok(Instruction {
            light_action,
            x_from,
            x_to,
            y_from,
            y_to,
        })
    }
}

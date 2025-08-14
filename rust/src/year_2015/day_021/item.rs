use regex::Regex;
use std::{
    fmt::Display,
    io::{self, Error, ErrorKind},
};

#[derive(Debug, Clone)]
pub struct Item {
    pub name: String,
    pub cost: usize,
    pub damage: usize,
    pub armor: usize,
}

impl Item {
    pub fn new(name: String, cost: usize, damage: usize, armor: usize) -> Item {
        Item {
            name,
            cost,
            damage,
            armor,
        }
    }
    pub fn from_raw(raw: String) -> io::Result<Item> {
        let regex_raw = r"([A-z]+ \+?[0-9]?)[ ]+([0-9]+)[ ]+([0-9]+)[ ]+([0-9]+)";
        let regex = Regex::new(regex_raw).map_err(|e| Error::new(ErrorKind::InvalidInput, e))?;

        let captures = regex
            .captures(&raw)
            .ok_or_else(|| Error::new(ErrorKind::InvalidData, format!("Invalid line: {}", raw)))?;

        let name = captures[1].to_string();

        let cost = captures[2]
            .parse::<usize>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;

        let damage = captures[3]
            .parse::<usize>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;

        let armor = captures[4]
            .parse::<usize>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;

        Ok(Item::new(name, cost, damage, armor))
    }
}

impl Display for Item {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> Result<(), std::fmt::Error> {
        write!(
            f,
            "Item({}, {}, {}, {})",
            self.name, self.cost, self.damage, self.armor
        )
    }
}

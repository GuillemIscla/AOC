use regex::Regex;
use std::io::{self, Error, ErrorKind};

#[derive(Debug, Clone)]
pub struct Ingredient {
    #[allow(dead_code)]
    name: String,
    capacity: isize,
    durability: isize,
    flavor: isize,
    texture: isize,
    pub calories: usize,
}

impl Ingredient {
    pub fn from_raw(raw: &str) -> io::Result<Ingredient> {
        let regex_raw = "([A-z]+): capacity ([\\-]{0,1}[0-9]+), durability ([\\-]{0,1}[0-9]+), flavor ([\\-]{0,1}[0-9]+), texture ([\\-]{0,1}[0-9]+), calories ([\\-]{0,1}[0-9]+)";
        let regex = Regex::new(regex_raw).map_err(|e| Error::new(ErrorKind::InvalidInput, e))?;

        let captures = regex
            .captures(raw)
            .ok_or_else(|| Error::new(ErrorKind::InvalidData, format!("Invalid line: {}", raw)))?;

        let name = String::from(&captures[1]);
        let capacity = captures[2]
            .parse::<isize>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;
        let durability = captures[3]
            .parse::<isize>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;
        let flavor = captures[4]
            .parse::<isize>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;
        let texture = captures[5]
            .parse::<isize>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;
        let calories = captures[6]
            .parse::<usize>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;

        Ok(Ingredient {
            name,
            capacity,
            durability,
            flavor,
            texture,
            calories,
        })
    }

    pub fn new_zero() -> Ingredient {
        Ingredient {
            name: String::from("zero"),
            capacity: 0,
            durability: 0,
            flavor: 0,
            texture: 0,
            calories: 0,
        }
    }

    pub fn add(&self, other: &Ingredient) -> Ingredient {
        Ingredient {
            name: String::from("operation"),
            capacity: self.capacity + other.capacity,
            durability: self.durability + other.durability,
            flavor: self.flavor + other.flavor,
            texture: self.texture + other.texture,
            calories: self.calories + other.calories,
        }
    }

    pub fn mult(&self, scalar: usize) -> Ingredient {
        Ingredient {
            name: String::from("operation"),
            capacity: self.capacity * scalar as isize,
            durability: self.durability * scalar as isize,
            flavor: self.flavor * scalar as isize,
            texture: self.texture * scalar as isize,
            calories: self.calories * scalar,
        }
    }

    pub fn score(&self) -> isize {
        if self.capacity <= 0 || self.durability <= 0 || self.flavor <= 0 || self.texture <= 0 {
            0
        } else {
            self.capacity * self.durability * self.flavor * self.texture
        }
    }
}

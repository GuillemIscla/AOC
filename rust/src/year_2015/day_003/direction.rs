use std::io::{self, Error, ErrorKind};

#[derive(PartialEq, Debug)]
pub enum Direction {
    North,
    South,
    East,
    West,
}

impl Direction {
    pub fn from_char(c: char) -> io::Result<Direction> {
        match c {
            '^' => Ok(Direction::North),
            'v' => Ok(Direction::South),
            '>' => Ok(Direction::East),
            '<' => Ok(Direction::West),
            _ => Err(Error::new(
                ErrorKind::InvalidData,
                format!("Invalid direction symbol: {}", c),
            )),
        }
    }
}

use std::io;

#[derive(Debug, Clone, PartialEq)]
pub struct Tile(pub usize, pub usize);

impl Tile {
    pub fn new(s: &str) -> io::Result<Self> {
        let mut split = s.split(",");
        let one = split
            .nth(0)
            .ok_or_else(|| io::Error::other("value missing"))?
            .parse::<usize>()
            .map_err(|_| io::Error::new(io::ErrorKind::InvalidData, "cannot parse to usize"))?;

        let two = split
            .nth(0)
            .ok_or(io::Error::other("value missing"))?
            .parse::<usize>()
            .map_err(|_| io::Error::new(io::ErrorKind::InvalidData, "cannot parse to usize"))?;

        Ok(Tile(one, two))
    }

    pub fn rectangle(&self, other: &Tile) -> usize {
        (self.0.abs_diff(other.0) + 1) * (self.1.abs_diff(other.1) + 1)
    }
}

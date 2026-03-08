use std::io;

#[derive(Debug, Clone)]
pub struct JunctionBox(pub i64, pub i64, pub i64);

impl JunctionBox {
    pub fn new(s: &str) -> io::Result<Self> {
        let vec = s
            .split(",")
            .map(|coord_raw| {
                coord_raw.parse().map_err(|_| {
                    io::Error::new(
                        io::ErrorKind::InvalidInput,
                        format!("{} is not a number", coord_raw),
                    )
                })
            })
            .collect::<io::Result<Vec<i64>>>()?;
        match vec.as_slice() {
            [a, b, c] => Ok(JunctionBox(*a, *b, *c)),
            _ => Err(io::Error::new(
                io::ErrorKind::InvalidInput,
                "Expected exactly 3 elements",
            )),
        }
    }

    pub fn distance_square(&self, other: JunctionBox) -> i64 {
        (self.0 - other.0).pow(2) + (self.1 - other.1).pow(2) + (self.2 - other.2).pow(2)
    }
}

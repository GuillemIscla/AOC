use std::io;

#[derive(Debug)]
pub struct IdDatabase {
    pub ranges: Vec<(i64, i64)>,
}

impl IdDatabase {
    pub fn new(s: String) -> io::Result<Self> {
        let ranges = s
            .split("\n")
            .map(Self::parse_pair)
            .collect::<io::Result<Vec<(i64, i64)>>>()?;

        Ok(IdDatabase { ranges })
    }

    fn parse_pair(input: &str) -> io::Result<(i64, i64)> {
        let mut parts = input.split('-');

        let first = parts
            .next()
            .ok_or_else(|| io::Error::new(io::ErrorKind::InvalidInput, "Missing first value"))?
            .parse::<i64>()
            .map_err(|_| io::Error::new(io::ErrorKind::InvalidInput, "Invalid first number"))?;

        let second = parts
            .next()
            .ok_or_else(|| io::Error::new(io::ErrorKind::InvalidInput, "Missing second value"))?
            .parse::<i64>()
            .map_err(|_| io::Error::new(io::ErrorKind::InvalidInput, "Invalid second number"))?;

        if parts.next().is_some() {
            return Err(io::Error::new(
                io::ErrorKind::InvalidInput,
                "Too many parts",
            ));
        }

        Ok((first, second))
    }

    pub fn no_intersections(&self) -> Self {
        let mut aux_ranges = self.ranges.clone();
        aux_ranges.sort();

        let mut upper_bound = 0;

        let ranges: Vec<(i64, i64)> = aux_ranges
            .iter()
            .enumerate()
            .flat_map(|(index, &(start, end))| {
                upper_bound = std::cmp::max(end, upper_bound);
                if index == aux_ranges.len() - 1 {
                    Some((start, upper_bound))
                } else if aux_ranges[index + 1].0 == start {
                    None
                } else {
                    let new_end = std::cmp::min(upper_bound, aux_ranges[index + 1].0 - 1);
                    Some((start, new_end))
                }
            })
            .collect();

        IdDatabase { ranges }
    }
}

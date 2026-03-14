use std::io;

#[derive(Debug)]
pub struct Present {
    pub shape: Vec<Vec<bool>>,
    pub total_size: usize,
    pub small_size: usize,
}

impl Present {
    pub fn new(lines: &[&str]) -> io::Result<Self> {
        let shape = lines
            .iter()
            .map(|line| line.trim().chars().map(|c| c == '#').collect::<Vec<bool>>())
            .collect::<Vec<Vec<bool>>>();

        let total_size = shape.iter().map(|row| row.len()).sum();

        let small_size = shape
            .iter()
            .map(|row| row.iter().filter(|&&b| b).count())
            .sum();

        Ok(Present {
            shape,
            total_size,
            small_size,
        })
    }
}

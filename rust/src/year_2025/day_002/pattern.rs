use std::io;

#[derive(Clone)]
pub struct Pattern {
    pub digits: Vec<Option<u8>>,
}

impl Pattern {
    pub fn new(size: usize) -> Pattern {
        let digits = vec![None; size];
        Pattern { digits }
    }

    pub fn set(&mut self, position: usize, value: u8) {
        self.digits[position] = Some(value);
    }

    pub fn has_sub_patterns(&self) -> io::Result<bool> {
        let digits = self
            .digits
            .iter()
            .map(|d| {
                d.ok_or(io::Error::new(
                    io::ErrorKind::InvalidData,
                    "Cannot perform calculation with a pattern not fully defined",
                ))
            })
            .collect::<io::Result<Vec<u8>>>()?;

        for i in 1..digits.len() {
            if digits.len() % i == 0 {
                let k = digits.len() / i;
                let mut has_sub_patterns = true;
                for j in 0..i {
                    for l in 0..k {
                        if digits[j] != digits[j + i * l] {
                            has_sub_patterns = false;
                            break;
                        }
                    }
                }
                if has_sub_patterns {
                    return Ok(true);
                }
            }
        }

        Ok(false)
    }

    pub fn to_u64(&self, times_it_fills: usize) -> io::Result<u64> {
        let mut result: u64 = 0;
        for _ in 0..times_it_fills {
            for digit in &self.digits {
                let digit_defined = digit.ok_or(io::Error::new(
                    io::ErrorKind::InvalidData,
                    "Cannot perform calculation with pattern not fully defined",
                ))?;

                result *= 10;
                result += digit_defined as u64;
            }
        }
        Ok(result)
    }
}

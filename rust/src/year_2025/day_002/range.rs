use super::pattern::Pattern;
use std::collections::HashSet;

#[derive(Debug, Clone)]
pub struct Range {
    pub low: u64,
    pub high: u64,
}

impl Range {
    // Divides in ranges where low and high have the same number of digits
    fn sub_ranges(&self) -> Vec<Range> {
        if self.low.to_string().len() == self.high.to_string().len() {
            vec![self.clone()]
        } else {
            let nines = 10_u64.pow(self.low.to_string().len().try_into().unwrap()) - 1;
            let first = Range {
                low: self.low,
                high: nines,
            };
            let second = Range {
                low: nines + 1,
                high: self.high,
            };
            let mut second_sub_ranges = second.sub_ranges();
            second_sub_ranges.insert(0, first);
            second_sub_ranges
        }
    }

    pub fn invalid_ids_sum(&self, pattern_repetition: Option<u8>) -> u64 {
        self.sub_ranges()
            .into_iter()
            .map(|sub_range| sub_range.invalid_ids_from_sub_range(pattern_repetition))
            .collect::<HashSet<_>>()
            .into_iter()
            .sum()
    }

    fn invalid_ids_from_sub_range(&self, pattern_repetition: Option<u8>) -> u64 {
        let low_digits = Self::to_digits(self.low);
        let mut result = 0;
        for pattern_size in 1..low_digits.len() {
            match pattern_repetition {
                Some(pattern_repetition_defined) => {
                    if low_digits.len() == pattern_size * pattern_repetition_defined as usize {
                        result += self.fill_pattern(Pattern::new(pattern_size), true, true, false);
                    }
                }
                None => {
                    if low_digits.len().is_multiple_of(pattern_size) {
                        result += self.fill_pattern(Pattern::new(pattern_size), true, true, true);
                    }
                }
            }
        }
        result
    }

    fn to_digits(int: u64) -> Vec<u8> {
        let mut copy = int;
        let mut result = Vec::new();
        while copy > 0 {
            result.insert(0, (copy % 10) as u8);
            copy /= 10;
        }
        result
    }

    fn fill_pattern(
        &self,
        pattern: Pattern,
        strict_low: bool,
        strict_high: bool,
        disregard_if_sub_patterns: bool,
    ) -> u64 {
        let low_digits = Self::to_digits(self.low);
        let high_digits = Self::to_digits(self.high);

        let mut i = 0;
        while i < pattern.digits.len() {
            if pattern.digits[i].is_none() {
                break;
            }
            i += 1;
        }

        if i == pattern.digits.len() {
            let times_it_fills = low_digits.len() / pattern.digits.len();
            match pattern.to_u64(times_it_fills) {
                Ok(invalid_id_unique)
                    if invalid_id_unique >= self.low
                        && invalid_id_unique <= self.high
                        && (!disregard_if_sub_patterns || !pattern.has_sub_patterns().unwrap()) =>
                {
                    return invalid_id_unique;
                }
                _ => return 0,
            }
        }

        let mut acc = 0;
        let low_boundary = if strict_low { low_digits[i] } else { 0 };
        let high_boundary = if strict_high { high_digits[i] } else { 9 };

        for j in low_boundary..=high_boundary {
            let mut new_pattern = pattern.clone();
            new_pattern.set(i, j);
            acc += self.fill_pattern(
                new_pattern,
                strict_low && j == low_boundary,
                strict_high && j == high_boundary,
                disregard_if_sub_patterns,
            );
        }

        acc
    }
}

impl std::str::FromStr for Range {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let (low_str, high_str) = s
            .split_once('-')
            .ok_or("Expected exactly one comma")
            .map_err(|_| format!("'{}' is not parseable into a pair of ints", &s))?;

        let low = low_str
            .parse::<u64>()
            .map_err(|_| format!("'{}' is not parseable into an int", &low_str))?;
        let high = high_str
            .parse::<u64>()
            .map_err(|_| format!("'{}' is not parseable into an int", &high_str))?;

        Ok(Range { low, high })
    }
}

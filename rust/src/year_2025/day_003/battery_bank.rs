use std::str::FromStr;

#[derive(Debug)]
pub struct BatteryBank {
    pub batteries: Vec<u8>,
}

impl BatteryBank {
    fn max_battery_index(&self, maybe_top: Option<u8>) -> usize {
        let filtered_batteries = self
            .batteries
            .iter()
            .enumerate()
            .filter(|&(_, &value)| maybe_top.is_none_or(|top| value < top))
            .collect::<Vec<(usize, &u8)>>();

        match filtered_batteries.first() {
            Some((max_index_from_original, max_from_original)) => {
                let mut max_index: usize = *max_index_from_original;
                let mut max: u8 = **max_from_original;
                filtered_batteries.iter().for_each(|&(index, &value)| {
                    if max < value {
                        max_index = index;
                        max = value;
                    }
                });
                max_index
            }
            None => 0,
        }
    }

    pub fn max_joltage(&self, batteries_size: u8) -> u64 {
        let mut max_index = self.max_battery_index(None);

        if batteries_size == 1 {
            self.batteries[max_index] as u64
        } else {
            while max_index > self.batteries.len() - batteries_size as usize {
                max_index = self.max_battery_index(Some(self.batteries[max_index]));
            }

            let sub_bank = BatteryBank {
                batteries: self.batteries[max_index + 1..].to_vec(),
            };

            let rest_joltage = sub_bank.max_joltage(batteries_size - 1);

            (self.batteries[max_index] as u64 * 10u64.pow(batteries_size as u32 - 1)) + rest_joltage
        }
    }
}

impl FromStr for BatteryBank {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let batteries = s
            .chars()
            .map(|c| {
                c.to_string()
                    .parse::<u8>()
                    .map_err(|_| format!("Could not convert '{}' into a u8", c))
            })
            .collect::<Result<Vec<u8>, Self::Err>>()?;

        Ok(BatteryBank { batteries })
    }
}

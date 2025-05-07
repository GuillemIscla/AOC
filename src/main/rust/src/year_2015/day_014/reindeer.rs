use regex::Regex;
use std::cmp;
use std::io::{self, Error, ErrorKind};

#[derive(Debug)]
pub struct Reindeer {
    pub name: String,
    pub speed_km_s: usize,
    fly_duration_s: usize,
    rest_duration_s: usize,
}

impl Reindeer {
    pub fn from_raw(line: &str) -> io::Result<Reindeer> {
        let regex_raw = "([A-z]+) can fly ([0-9]+) km/s for ([0-9]+) seconds, but then must rest for ([0-9]+) seconds.";
        let regex = Regex::new(regex_raw).map_err(|e| Error::new(ErrorKind::InvalidInput, e))?;

        let captures = regex
            .captures(line)
            .ok_or_else(|| Error::new(ErrorKind::InvalidData, format!("Invalid line: {}", line)))?;

        let name = String::from(&captures[1]);
        let speed_km_s = captures[2]
            .parse::<usize>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;
        let fly_duration_s = captures[3]
            .parse::<usize>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;
        let rest_duration_s = captures[4]
            .parse::<usize>()
            .map_err(|e| Error::new(ErrorKind::InvalidData, e))?;

        Ok(Reindeer {
            name,
            speed_km_s,
            fly_duration_s,
            rest_duration_s,
        })
    }

    pub fn distance_in_race(&self, seconds: usize) -> usize {
        let complete_cicles = seconds / (self.fly_duration_s + self.rest_duration_s);
        let distance_complete_cicles = complete_cicles * self.speed_km_s * self.fly_duration_s;

        let duration_reminder_cicle = seconds % (self.fly_duration_s + self.rest_duration_s);
        let fly_duration_reminder_cicle = cmp::min(duration_reminder_cicle, self.fly_duration_s);
        let distance_reminder_cicle = fly_duration_reminder_cicle * self.speed_km_s;

        distance_complete_cicles + distance_reminder_cicle
    }

    pub fn speed_at_time(&self, seconds: usize) -> usize {
        let duration_reminder_cicle = seconds % (self.fly_duration_s + self.rest_duration_s);
        if duration_reminder_cicle < self.fly_duration_s {
            self.speed_km_s
        } else {
            0
        }
    }

    pub fn next_speed_change(&self, seconds: usize, change_is_fly: bool) -> usize {
        let complete_cicles = seconds / (self.fly_duration_s + self.rest_duration_s);
        let cicle_remainder = seconds % (self.fly_duration_s + self.rest_duration_s);
        if change_is_fly || cicle_remainder >= self.fly_duration_s {
            (complete_cicles + 1) * (self.fly_duration_s + self.rest_duration_s)
        } else if cicle_remainder >= self.fly_duration_s {
            (complete_cicles + 1) * (self.fly_duration_s + self.rest_duration_s)
                + self.fly_duration_s
        } else {
            complete_cicles * (self.fly_duration_s + self.rest_duration_s) + self.fly_duration_s
        }
    }
}

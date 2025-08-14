use super::day_014::reindeer::Reindeer;
use std::collections::HashMap;

use parse::*;
use race_utils::*;

mod parse;
mod race_utils;
mod reindeer;

#[allow(dead_code)]
pub fn part_1() {
    let reindeers = parse(true);
    let race_duration = 2503;
    let max_distance = reindeers
        .into_iter()
        .map(|reindeer| reindeer.distance_in_race(race_duration))
        .max()
        .unwrap();

    println!("Part 1: {}", max_distance);
}

//The part_2 calculates the winner in each segment.
#[allow(dead_code)]
pub fn part_2() {
    let reindeers = parse(true);
    let race_duration = 2503;
    let mut points: HashMap<String, usize> = HashMap::new();
    let mut race_current = run_race_segment(1, &mut points, &reindeers, race_duration);
    while race_current <= race_duration {
        race_current = run_race_segment(race_current, &mut points, &reindeers, race_duration);
    }
    let max_points = points.iter().max_by(|a, b| a.1.cmp(b.1)).unwrap().1;

    println!("Part 2: {}", max_points);
    part_2_slow(reindeers, race_duration);
}

//The part_2_slow calculates the winner in each second.
#[allow(dead_code)]
pub fn part_2_slow(reindeers: Vec<Reindeer>, race_duration: usize) {
    let mut points: HashMap<String, usize> = HashMap::new();

    for race_current in 1..race_duration + 1 {
        let leads = get_leads(&reindeers, race_current);
        leads.iter().for_each(|lead| {
            match points.get(&lead.name) {
                Some(old_points) => points.insert(lead.name.to_string(), old_points + 1),
                None => points.insert(lead.name.to_string(), 1),
            };
        });
    }
    let max_points = points.iter().max_by(|a, b| a.1.cmp(b.1)).unwrap().1;

    println!("Part 2: {} (slow computation)", max_points);
}

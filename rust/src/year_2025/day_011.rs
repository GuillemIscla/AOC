use crate::year_2025::day_011::path_checkpoint_counter::PathCheckpointCounter;
use crate::year_2025::day_011::utils::*;
use parse::*;
use std::collections::HashMap;

mod device;
mod parse;
mod path_checkpoint_counter;
mod utils;

#[allow(dead_code)]
pub fn part_1() {
    let devices = parse(None);

    let mut cache: HashMap<String, u32> = HashMap::new();
    cache.insert("out".to_string(), 1);
    let path_count = path_count_deep_first("you".to_string(), &devices, &mut cache);

    println!("Part 1: {}", path_count);
}

#[allow(dead_code)]
pub fn part_2() {
    let devices = parse(None);

    let mut cache: HashMap<String, PathCheckpointCounter> = HashMap::new();
    cache.insert(
        "out".to_string(),
        PathCheckpointCounter {
            only_fft: 0,
            only_dac: 0,
            both: 0,
            neither: 1,
        },
    );
    let path_checkpoint_count =
        path_checkpoint_count_deep_first("svr".to_string(), &devices, &mut cache);

    println!("Part 2: {}", path_checkpoint_count.both);
}

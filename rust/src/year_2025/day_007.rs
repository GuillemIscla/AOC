use crate::year_2025::day_007::tachyon_diagram::TachyonBeamResult;
use parse::*;

mod parse;
mod tachyon_diagram;

#[allow(dead_code)]
pub fn part_1() {
    let mut tachyon_diagram = parse(None);

    let TachyonBeamResult { splits, .. } = tachyon_diagram.shoot_beam();

    println!("Part 1: {}", splits);
}

#[allow(dead_code)]
pub fn part_2() {
    let mut tachyon_diagram = parse(None);

    let TachyonBeamResult {
        timeline_counters, ..
    } = tachyon_diagram.shoot_beam();

    println!("Part 2: {}", timeline_counters.values().sum::<u64>());
}

use parse::*;

use crate::year_2025::day_010::utils::combinations;

mod computation_output;
mod factory_machine;
mod parse;
mod restriction;
mod utils;

#[allow(dead_code)]
pub fn part_1() {
    let factory_machines = parse(None);

    let fewer_presses = factory_machines
        .into_iter()
        .map(|factory_machine| {
            let mut fewer_presses: usize = 0;
            for m in 1..factory_machine.button_wiring_schematics.len() {
                if combinations(factory_machine.button_wiring_schematics.len(), m)
                    .iter()
                    .any(|combination| factory_machine.try_combination(combination))
                {
                    fewer_presses = m;
                    break;
                }
            }
            fewer_presses
        })
        .collect::<Vec<usize>>();

    println!("Part 1: {}", fewer_presses.into_iter().sum::<usize>());
}

#[allow(dead_code)]
pub fn part_2() {
    let factory_machines = parse(None);

    let result = factory_machines
        .iter()
        .map(|factory_machine| factory_machine.fewer_for_joltage().iter().sum::<usize>())
        .sum::<usize>();

    println!("Part 2: {}", result);
}

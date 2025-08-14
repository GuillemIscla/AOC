use parse::*;

mod element;
mod laboratory;
mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let laboratory = parse(true);
    let target_molecule = laboratory.target_molecule.clone();

    println!(
        "Part 1: {}",
        laboratory.process_replacements(&target_molecule).len()
    );
}

#[allow(dead_code)]
pub fn part_2() {
    let laboratory = parse(true);

    let result = laboratory.formula_to_reduce();
    println!("Part 2: {}", result);
}

use crate::year_2015::vector_utils::*;
use parse::*;

mod arrangement_notes;
mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let arrangement_notes = parse(true);

    let permutations: Vec<Vec<usize>> = get_permutations(arrangement_notes.person_count())
        .into_iter()
        .filter(|permutation| permutation.first().into_iter().sum::<usize>() == 0)
        .collect();

    let max_happiness = permutations
        .iter()
        .map(|permutation| arrangement_notes.permute_and_evaluate(permutation.to_vec()))
        .max()
        .unwrap();

    println!("Part 1: {}", max_happiness);
}

//41 not right
#[allow(dead_code)]
pub fn part_2() {
    let mut arrangement_notes = parse(true);
    arrangement_notes.add_ambivalent(String::from("Myself"));

    let permutations: Vec<Vec<usize>> = get_permutations(arrangement_notes.person_count())
        .into_iter()
        .filter(|permutation| permutation.first().into_iter().sum::<usize>() == 0)
        .collect();

    let max_happiness = permutations
        .iter()
        .map(|permutation| arrangement_notes.permute_and_evaluate(permutation.to_vec()))
        .max()
        .unwrap();

    println!("Part 2: {}", max_happiness);
}

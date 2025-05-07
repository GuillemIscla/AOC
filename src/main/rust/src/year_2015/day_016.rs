use aunt_sue_ticker_tape::AuntSueTickerTape;

use crate::year_2015::day_016::parse::*;

mod aunt_sue_notes;
mod aunt_sue_ticker_tape;
mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let aunts_sue_notes = parse(true);
    let aunt_sue_ticker_tape = AuntSueTickerTape {
        children: 3,
        cats: 7,
        samoyeds: 2,
        pomeranians: 3,
        akitas: 0,
        vizslas: 0,
        goldfish: 5,
        trees: 3,
        cars: 2,
        perfumes: 1,
    };

    let aunt_sue_result = aunts_sue_notes
        .into_iter()
        .find(|aunt_sue_notes| aunt_sue_ticker_tape.match_with_notes(aunt_sue_notes))
        .expect("No matches in notes for this ticker tape");

    println!("Part 1: {}", aunt_sue_result.number);
}

#[allow(dead_code)]
pub fn part_2() {
    let aunts_sue_notes = parse(true);
    let aunt_sue_ticker_tape = AuntSueTickerTape {
        children: 3,
        cats: 7,
        samoyeds: 2,
        pomeranians: 3,
        akitas: 0,
        vizslas: 0,
        goldfish: 5,
        trees: 3,
        cars: 2,
        perfumes: 1,
    };

    let aunt_sue_result = aunts_sue_notes
        .into_iter()
        .find(|aunt_sue_notes| aunt_sue_ticker_tape.match_with_notes_modified(aunt_sue_notes))
        .expect("No matches in notes for this ticker tape");

    println!("Part 2: {}", aunt_sue_result.number);
}

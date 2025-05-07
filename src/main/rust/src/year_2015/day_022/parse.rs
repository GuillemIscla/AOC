use super::game::Game;
use super::warrior::Warrior;
use super::wizard::Wizard;
use indoc::indoc;
use std::fs::File;
use std::io::{self, BufRead};

pub fn parse(from_file: bool) -> Game {
    switch_sample_and_parse(from_file).expect("Error on parsing")
}

fn switch_sample_and_parse(from_file: bool) -> io::Result<Game> {
    let file_path = "inputs/year_2015/day_022.txt";
    let file: File = File::open(file_path)?;

    if from_file {
        let player_raw = indoc! {"Hit Points: 50
        Mana Points: 500"}
        .trim_start()
        .split("\n")
        .map(String::from)
        .collect::<Vec<String>>();

        let player = Wizard::from_raw(player_raw)?;

        let boss_raw = io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()?;

        let boss = Warrior::from_raw(boss_raw)?;

        Ok(Game::new(player, boss))
    } else {
        let player_raw = indoc! {"Hit Points: 10
        Damage: 250"}
        .trim_start()
        .split("\n")
        .map(String::from)
        .collect::<Vec<String>>();

        let player = Wizard::from_raw(player_raw)?;

        let boss_raw = indoc! {"Hit Points: 14
        Damage: 8"}
        .trim_start()
        .split("\n")
        .map(String::from)
        .collect::<Vec<String>>();

        let boss = Warrior::from_raw(boss_raw)?;

        Ok(Game::new(player, boss))
    }
}

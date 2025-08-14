use super::game::Game;
use super::item::Item;
use super::warrior::Warrior;
use indoc::indoc;
use std::fs::File;
use std::io::{self, BufRead};

pub fn parse(from_file: bool) -> Game {
    switch_sample_and_parse(from_file).expect("Error on parsing")
}

fn switch_sample_and_parse(from_file: bool) -> io::Result<Game> {
    let file_path = "inputs/year_2015/day_021.txt";
    let file: File = File::open(file_path)?;

    let weapons_raw = indoc! {"Dagger        8     4       0
                                   Shortsword   10     5       0
                                   Warhammer    25     6       0
                                   Longsword    40     7       0
                                   Greataxe     74     8       0"}
    .trim_start()
    .split("\n")
    .map(String::from)
    .collect::<Vec<String>>();

    let weapons = weapons_raw
        .into_iter()
        .map(Item::from_raw)
        .collect::<io::Result<Vec<Item>>>()?;

    let armor_raw = indoc! {"Leather      13     0       1
                                 Chainmail    31     0       2
                                 Splintmail   53     0       3
                                 Bandedmail   75     0       4
                                 Platemail   102     0       5"}
    .trim_start()
    .split("\n")
    .map(String::from)
    .collect::<Vec<String>>();

    let armor = armor_raw
        .into_iter()
        .map(Item::from_raw)
        .collect::<io::Result<Vec<Item>>>()?;

    let rings_raw = indoc! {"Damage +1    25     1       0
                                 Damage +2    50     2       0
                                 Damage +3   100     3       0
                                 Defense +1   20     0       1
                                 Defense +2   40     0       2
                                 Defense +3   80     0       3"}
    .trim_start()
    .split("\n")
    .map(String::from)
    .collect::<Vec<String>>();

    let rings = rings_raw
        .into_iter()
        .map(Item::from_raw)
        .collect::<io::Result<Vec<Item>>>()?;

    if from_file {
        let player_raw = indoc! {"Hit Points: 100
                                  Damage: 0
                                  Armor: 0"}
        .trim_start()
        .split("\n")
        .map(String::from)
        .collect::<Vec<String>>();

        let player = Warrior::from_raw(player_raw)?;

        let boss_raw = io::BufReader::new(file)
            .lines()
            .collect::<io::Result<Vec<String>>>()?;

        let boss = Warrior::from_raw(boss_raw)?;

        Ok(Game::new(player, boss, weapons, armor, rings))
    } else {
        let player_raw = indoc! {"Hit Points: 8
                                  Damage: 5
                                  Armor: 5"}
        .trim_start()
        .split("\n")
        .map(String::from)
        .collect::<Vec<String>>();

        let player = Warrior::from_raw(player_raw)?;

        let boss_raw = indoc! {"Hit Points: 12
                                  Damage: 7
                                  Armor: 2"}
        .trim_start()
        .split("\n")
        .map(String::from)
        .collect::<Vec<String>>();

        let boss = Warrior::from_raw(boss_raw)?;

        Ok(Game::new(player, boss, weapons, armor, rings))
    }
}

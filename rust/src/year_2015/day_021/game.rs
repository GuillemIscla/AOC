use super::item::Item;
use super::warrior::Warrior;
use itertools::{iproduct, Itertools};
use std::cmp;
use std::fmt::Display;

#[derive(Debug)]
pub struct Game {
    player: Warrior,
    boss: Warrior,
    weapons: Vec<Item>,
    armor: Vec<Item>,
    rings: Vec<Item>,
}

impl Game {
    pub fn new(
        player: Warrior,
        boss: Warrior,
        weapons: Vec<Item>,
        armor: Vec<Item>,
        rings: Vec<Item>,
    ) -> Game {
        Game {
            player,
            boss,
            weapons,
            armor,
            rings,
        }
    }

    pub fn player_vs_boss(&self, print: bool) -> bool {
        let mut player = self.player.clone();
        let mut boss = self.boss.clone();
        let mut player_turn = true;
        while player.hit_points > 0 && boss.hit_points > 0 {
            let (attacker, defender, attacker_name, defender_name) = if player_turn {
                (&player, &mut boss, "player", "boss")
            } else {
                (&boss, &mut player, "boss", "player")
            };

            let damage = cmp::max(
                1,
                attacker.get_damage() as isize - defender.get_armor() as isize,
            );

            defender.hit_points -= damage;
            if print {
                println!(
                    "The {} deals {}-{}={} damage; the {} goes down to {} hit points",
                    attacker_name,
                    attacker.get_damage(),
                    defender.get_armor(),
                    damage,
                    defender_name,
                    defender.hit_points
                )
            }

            player_turn = !player_turn;
        }

        player.hit_points > 0
    }

    pub fn min_weapon_cost(&self) -> usize {
        self.weapons.iter().map(|weapon| weapon.cost).min().unwrap()
    }

    pub fn max_you_can_spend(&self) -> usize {
        let weapon_cost: usize = self.weapons.iter().map(|weapon| weapon.cost).max().unwrap();
        let armor_cost: usize = self.armor.iter().map(|armor| armor.cost).max().unwrap();
        let rings_cost: usize = self
            .rings
            .iter()
            .map(|ring| ring.cost)
            .sorted()
            .rev()
            .take(2)
            .sum();

        weapon_cost + armor_cost + rings_cost
    }

    pub fn win_lose_one_game_with_gold(
        &mut self,
        gold: usize,
        aim_to_win: bool,
        print: bool,
    ) -> bool {
        let item_sets = self.buy_items(gold);
        if item_sets.is_empty() {
            return false;
        }

        item_sets.iter().any(|item_set| {
            self.player.items = item_set.to_vec();
            let result = self.player_vs_boss(print);
            self.player.items = Vec::new();
            if aim_to_win {
                result
            } else {
                !result
            }
        })
    }

    fn buy_items(&self, gold: usize) -> Vec<Vec<Item>> {
        let weapon_indices: Vec<usize> = (0..self.weapons.len()).collect();

        let up_to_one_armor_indices: Vec<Option<usize>> = (0..=self.armor.len())
            .map(|index| {
                if index < self.armor.len() {
                    Some(index)
                } else {
                    None
                }
            })
            .collect();

        let up_to_one_ring_indices: Vec<Option<usize>> = (0..=self.rings.len())
            .map(|index| {
                if index < self.rings.len() {
                    Some(index)
                } else {
                    None
                }
            })
            .collect();

        let copy = up_to_one_ring_indices.clone();
        let up_to_two_rings_indices: Vec<(Option<usize>, Option<usize>)> =
            iproduct!(up_to_one_ring_indices, copy)
                .filter(|(a, b)| match (a, b) {
                    (Some(a), Some(b)) if a == b => false, //cannot pick the same ring twice
                    (Some(_), Some(_)) => true,            // we can pick two rings
                    (Some(_), None) => true,               //we can pick one ring
                    (None, Some(_)) => false, //if we pick only one ring, is the first pick, no repetition of choices
                    (None, None) => true,     //we can pick no rings
                })
                .collect();

        iproduct!(
            iproduct!(weapon_indices, up_to_one_armor_indices),
            up_to_two_rings_indices
        )
        .filter_map(
            |((weapon_index, armor_index), (ring_1_index, ring_2_index))| {
                let weapon = self.weapons[weapon_index].clone();
                let armor = armor_index.map(|armor_index| self.armor[armor_index].clone());
                let ring_1 = ring_1_index.map(|ring_1_index| self.rings[ring_1_index].clone());
                let ring_2 = ring_2_index.map(|ring_2_index| self.rings[ring_2_index].clone());

                let cost = weapon.cost
                    + armor.clone().map_or(0, |armor| armor.cost)
                    + ring_1.clone().map_or(0, |ring_1| ring_1.cost)
                    + ring_2.clone().map_or(0, |ring_2| ring_2.cost);

                if cost != gold {
                    None
                } else {
                    let mut result = Vec::new();
                    result.push(weapon);
                    armor.into_iter().for_each(|armor| result.push(armor));
                    ring_1.into_iter().for_each(|ring_1| result.push(ring_1));
                    ring_2.into_iter().for_each(|ring_2| result.push(ring_2));
                    Some(result)
                }
            },
        )
        .collect::<Vec<Vec<Item>>>()
    }
}

impl Display for Game {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> Result<(), std::fmt::Error> {
        write!(
            f,
            "Game(\nplayer: {}, \nboss: {}, \nweapons: {:?}, \narmor: {:?}, \nrings: {:?})",
            self.player, self.boss, self.weapons, self.armor, self.rings
        )
    }
}

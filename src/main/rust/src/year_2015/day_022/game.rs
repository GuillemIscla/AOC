use super::spell::{EffectOrInstant, Spell};
use super::warrior::Warrior;
use super::wizard::Wizard;
use std::cmp;
use std::fmt::Display;

#[derive(Debug)]
pub struct Game {
    pub player: Wizard,
    pub boss: Warrior,
}

impl Game {
    pub fn new(player: Wizard, boss: Warrior) -> Game {
        Game { player, boss }
    }

    //Simulation of a battle where the spells to cast are decided upfront
    fn player_vs_boss(&self, spells_to_cast: Vec<Spell>, hard: bool, print: bool) -> bool {
        let mut player = self.player.clone();
        let mut boss = self.boss.clone();
        let mut player_turn = true;
        let mut spell_to_cast_index = 0;
        let mut active_spells: Vec<Spell> = Vec::new();
        while player.hit_points > 0 && boss.hit_points > 0 {
            if print {
                if player_turn {
                    println!("-- Player turn --");
                } else {
                    println!("-- Boss turn --");
                }
                println!(
                    "- Player has {} hit points, {} armor, {} mana",
                    player.hit_points, player.armor, player.mana_points
                );
                println!("- Boss has {} hit points", boss.hit_points);
            }

            //If the level is hard player loses 1 point in his turn
            if player_turn && hard {
                player.hit_points -= 1;
                if print {
                    println!("Player looses 1 life point due to the level being hard");
                }
                if player.hit_points <= 0 {
                    break;
                }
            }

            //We substract the turns out of the active spells
            for spell in active_spells.iter_mut() {
                spell.decrease_timer();
            }

            //We go through all the active effect consequences
            //We also drop them if they have not enough turns to be here
            active_spells.retain(|active_spell| match active_spell.effect_or_instant {
                EffectOrInstant::Effect {
                    timer,
                    in_turn,
                    on: _,
                    off,
                } => {
                    in_turn(&mut player, &mut boss, print, timer);
                    if timer == 0 {
                        off(&mut player, &mut boss, print);
                        false
                    } else {
                        true
                    }
                }
                EffectOrInstant::Instant { consequence: _ } => false,
            });

            if boss.hit_points <= 0 {
                break;
            }

            if player_turn {
                //Determine the current spell
                let current_spell = match spells_to_cast.get(spell_to_cast_index) {
                    Some(current_spell) => {
                        //Player cannot cast a spell when is active. If so he loses
                        //This is not expected to happen due to the function that selects spells
                        if active_spells
                            .iter()
                            .any(|active_spell| active_spell.name == current_spell.name)
                        {
                            println!("Player tries to cast a spell when is already in effect. Player loses.");
                            return false;
                        }

                        //Player needs to have enough mana points to cast a spell if not so, he loses
                        match player.mana_points.checked_sub(current_spell.cost) {
                            Some(substract) => player.mana_points = substract,
                            None => {
                                if print {
                                    println!(
                                        "Player has no mana to cast the next spell. Player loses."
                                    );
                                }
                                return false;
                            }
                        }
                        current_spell.clone()
                    }
                    None => {
                        //Player has not enough spells prepared, he loses.
                        if print {
                            println!("Player has not plan a next spell. Player loses.");
                        }
                        return false;
                    }
                };

                spell_to_cast_index += 1;

                //We apply the initial consequence of casting the spell
                match current_spell.effect_or_instant {
                    EffectOrInstant::Effect {
                        timer: _,
                        in_turn: _,
                        on,
                        off: _,
                    } => on(&mut player, &mut boss, print),
                    EffectOrInstant::Instant { consequence } => {
                        consequence(&mut player, &mut boss, print)
                    }
                }

                //We add the spell so next turns have its potential effects
                active_spells.push(current_spell);
            }

            //Boss logic is much simpler
            if !player_turn {
                let damage: isize = cmp::max(1, boss.damage - player.armor) as isize;
                player.hit_points -= damage;
                if print {
                    if player.armor == 0 {
                        println!("Boss attacks for {} damage!", boss.damage);
                    } else {
                        println!(
                            "Boss attacks for {} - {} = {} damage!",
                            boss.damage, player.armor, damage
                        );
                    }
                }
            }

            player_turn = !player_turn;
            if print {
                println!();
            }
        }

        if print {
            if boss.hit_points <= 0 {
                println!("This kills the boss, and the player wins.");
            } else {
                println!("This kills the player, and the boss wins.");
            }
            println!();
        }

        player.hit_points > 0
    }

    //Calculates wether is possible to win with exactly this mana
    //First calls an heuristic method that calculates possible spells sets
    //Then we try each spell set in the actual algorithm
    pub fn win_with_mana(&self, mana_to_use: usize, hard: bool, print: bool) -> bool {
        let spell_sets = Self::prepare_spells(
            self.player.mana_points,
            mana_to_use,
            hard,
            &self.player,
            &self.boss,
        );

        spell_sets
            .into_iter()
            .any(|spells_to_cast| self.player_vs_boss(spells_to_cast, hard, print))
    }

    pub fn prepare_spells(
        initial_mana: usize,
        mana_to_use: usize,
        hard: bool,
        player: &Wizard,
        boss: &Warrior,
    ) -> Vec<Vec<Spell>> {
        Self::prepare_spells_internal(initial_mana, mana_to_use, hard, player, boss, Vec::new())
    }

    //Recursive function to calculate all the possible spells that can be casted with exactly this
    //much mana. Heurictically, it disregards spell sets: if any spell that is left out would succeed, the algoritm
    //provides other spell sets that will succeed.
    fn prepare_spells_internal(
        initial_mana: usize,
        mana_to_use: usize,
        hard: bool,
        player: &Wizard,
        boss: &Warrior,
        casted_before: Vec<Spell>,
    ) -> Vec<Vec<Spell>> {
        //We handle the base case where we already used the exact amount of mana
        if mana_to_use == 0 {
            if Game::boss_might_be_dead(&casted_before, boss) {
                return vec![casted_before];
            } else {
                return Vec::new();
            }
        }

        //We heuristically detect strategies bond to fail and trim the recursion
        if !Game::can_cast_spells(&casted_before, initial_mana)
            || (!Game::player_may_be_alive(&casted_before, hard, player, boss)
                && !Game::boss_might_be_dead(&casted_before, boss))
        {
            return Vec::new();
        }

        //Starting with instant spells is a bad strategy since we cand stand as much as a couple of
        //hits from the boss at the beginning and effect spells are more effective if they are in effect most of the
        //time possible.
        //
        //This is not a very straightforward insight but is true and trims the recursion quite a lot.
        let spells_to_cast = if casted_before.len() < 3 {
            vec![Spell::recharge(), Spell::shield(), Spell::poison()]
        } else {
            vec![
                Spell::recharge(),
                Spell::shield(),
                Spell::drain(),
                Spell::poison(),
                Spell::magic_missile(),
            ]
        };

        //For each spell we see if we can cast it and make a recursive call to figure out the rest
        //If we reached here we have postitive mana but if we cannot cast any spell from here, we
        //return empty
        spells_to_cast
            .into_iter()
            .flat_map(|spell_to_cast| {
                //If we cannot cast the spell we trim the recursion
                if spell_to_cast.cost > mana_to_use {
                    Vec::new()
                } else {
                    match spell_to_cast.effect_or_instant {
                        EffectOrInstant::Instant { consequence: _ } => {
                            //As long as we have mana, we can always cast an instant spell
                            let mut copy_casted_before = casted_before.clone();
                            let cost = spell_to_cast.cost;
                            copy_casted_before.push(spell_to_cast);
                            Self::prepare_spells_internal(
                                initial_mana,
                                mana_to_use.saturating_sub(cost),
                                hard,
                                player,
                                boss,
                                copy_casted_before,
                            )
                        }
                        EffectOrInstant::Effect {
                            timer,
                            in_turn: _,
                            on: _,
                            off: _,
                        } => {
                            //For an effect spell we need to check if we already casted it lately
                            let index = if timer % 2 == 0 {
                                casted_before.len().saturating_sub((timer / 2) - 1)
                            } else {
                                casted_before.len().saturating_sub(timer / 2)
                            };

                            let lately_casted = &casted_before[index..];
                            if lately_casted
                                .iter()
                                .any(|casted_pell| casted_pell.name == spell_to_cast.name)
                            {
                                Vec::new()
                            } else {
                                let mut copy_casted_before = casted_before.clone();
                                let cost = spell_to_cast.cost;
                                copy_casted_before.push(spell_to_cast);
                                Self::prepare_spells_internal(
                                    initial_mana,
                                    mana_to_use.saturating_sub(cost),
                                    hard,
                                    player,
                                    boss,
                                    copy_casted_before,
                                )
                            }
                        }
                    }
                }
            })
            .collect()
    }

    //Heuristic method.
    //Can return true but the player would actually die with this strategy
    //If it returns false, we are certain the player will die with this strategy
    pub fn player_may_be_alive(
        casted_before: &[Spell],
        hard: bool,
        player: &Wizard,
        boss: &Warrior,
    ) -> bool {
        //We assume all the armor spells are active the most possible time.
        //This part of heuristic turns out to be pretty accurate since we just have one possible armor
        //spell
        let max_armor_by_spell = casted_before
            .iter()
            .map(|spell| spell.total_armor)
            .max()
            .unwrap_or(0);
        let armor_spell_count = casted_before
            .iter()
            .filter(|spell| spell.total_armor > 0)
            .count();
        let max_armor_at_any_turn = max_armor_by_spell * armor_spell_count;
        let max_turns_with_armor = casted_before
            .iter()
            .map(|spell| match spell.effect_or_instant {
                EffectOrInstant::Effect {
                    timer,
                    in_turn: _,
                    on: _,
                    off: _,
                } if spell.total_armor > 0 => timer,
                _ => 0,
            })
            .sum();

        let min_damage_caused_with_armor = {
            let turns_with_armor = 2 * cmp::min(casted_before.len(), max_turns_with_armor);
            let damage_with_armor =
                cmp::max(1, boss.damage as isize - max_armor_at_any_turn as isize);
            turns_with_armor * damage_with_armor as usize
        };
        let min_damage_caused_without_armor = {
            let turns_with_armor = 2 * cmp::min(casted_before.len(), max_turns_with_armor);
            let turns_without_armor = 2 * casted_before.len() - turns_with_armor;
            let damage_without_armor = boss.damage;
            turns_without_armor * damage_without_armor
        };

        //We account for the loss of 1 point due to the level in the heuristic
        let damage_caused_by_level = if hard { casted_before.len() + 1 } else { 0 };

        let min_damage_caused =
            min_damage_caused_with_armor + min_damage_caused_without_armor + damage_caused_by_level;

        let max_heal: usize = casted_before.iter().map(|spell| spell.total_heal).sum();

        player.hit_points + max_heal as isize > min_damage_caused as isize
    }

    //Heuristic method.
    //Can return true but the boss would actually be alive with this strategy
    //If it returns false, we are certain the boss will be alive
    fn boss_might_be_dead(casted_before: &[Spell], boss: &Warrior) -> bool {
        let max_boss_damage: usize = casted_before.iter().map(|spell| spell.total_damage).sum();
        boss.hit_points <= max_boss_damage as isize
    }

    //Heuristic method.
    //Can return true but the player would actually lack mana if trying this spell sequence
    //If it returns false, we are certain the player won't be able to cast these spells
    fn can_cast_spells(casted_before: &[Spell], initial_mana: usize) -> bool {
        casted_before
            .iter()
            .map(|spell| spell.cost as isize - spell.total_mana as isize)
            .sum::<isize>()
            <= initial_mana as isize
    }
}

impl Display for Game {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> Result<(), std::fmt::Error> {
        write!(f, "Game(\nplayer: {}, \nboss: {})", self.player, self.boss)
    }
}

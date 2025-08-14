use super::warrior::Warrior;
use super::wizard::Wizard;
use std::fmt::Display;

#[derive(Debug, Clone)]
pub enum EffectOrInstant {
    Effect {
        timer: usize,
        in_turn: fn(&mut Wizard, &mut Warrior, print: bool, current_timer: usize),
        on: fn(&mut Wizard, &mut Warrior, print: bool),
        off: fn(&mut Wizard, &mut Warrior, print: bool),
    },
    Instant {
        consequence: fn(&mut Wizard, &mut Warrior, print: bool),
    },
}

#[derive(Debug, Clone)]
pub struct Spell {
    pub name: String,
    pub cost: usize,
    pub effect_or_instant: EffectOrInstant,
    pub total_heal: usize,
    pub total_mana: usize,
    pub total_armor: usize,
    pub total_damage: usize,
}

impl Spell {
    pub fn magic_missile() -> Spell {
        fn consequence(_wizard: &mut Wizard, warrior: &mut Warrior, print: bool) {
            warrior.hit_points -= 4;
            if print {
                println!("Player casts Magic Missile, dealing 4 damage.");
            }
        }
        Spell {
            name: "Magic Missile".to_string(),
            cost: 53,
            effect_or_instant: EffectOrInstant::Instant { consequence },
            total_heal: 0,
            total_mana: 0,
            total_armor: 0,
            total_damage: 4,
        }
    }

    pub fn drain() -> Spell {
        fn consequence(wizard: &mut Wizard, warrior: &mut Warrior, print: bool) {
            wizard.hit_points += 2;
            warrior.hit_points -= 2;
            if print {
                println!("Player casts Drain, dealing 2 manage, and healing 2 hit points.");
            }
        }
        Spell {
            name: "Drain".to_string(),
            cost: 73,
            effect_or_instant: EffectOrInstant::Instant { consequence },
            total_heal: 2,
            total_mana: 0,
            total_armor: 0,
            total_damage: 2,
        }
    }

    pub fn shield() -> Spell {
        fn in_turn(
            _wizard: &mut Wizard,
            _warrior: &mut Warrior,
            print: bool,
            current_timer: usize,
        ) {
            if print {
                println!("Shields timer is now {}.", current_timer);
            }
        }

        fn on(wizard: &mut Wizard, _warrior: &mut Warrior, print: bool) {
            wizard.armor = 7;
            if print {
                println!("Player casts Shield, increasing armor by 7.");
            }
        }

        fn off(wizard: &mut Wizard, _warrior: &mut Warrior, print: bool) {
            wizard.armor = 0;
            if print {
                println!("Shield wears off, decreasing armor by 7.");
            }
        }

        Spell {
            name: "Shield".to_string(),
            cost: 113,
            effect_or_instant: EffectOrInstant::Effect {
                timer: 6,
                in_turn,
                on,
                off,
            },
            total_heal: 0,
            total_mana: 0,
            total_armor: 7,
            total_damage: 0,
        }
    }

    pub fn poison() -> Spell {
        fn in_turn(_wizard: &mut Wizard, warrior: &mut Warrior, print: bool, current_timer: usize) {
            warrior.hit_points -= 3;
            if print {
                println!("Poison deals 3 damage; its timer is now {}.", current_timer);
            }
        }

        fn on(_wizard: &mut Wizard, _warrior: &mut Warrior, print: bool) {
            if print {
                println!("Player casts Poison.");
            }
        }

        fn off(_wizard: &mut Wizard, _warrior: &mut Warrior, print: bool) {
            if print {
                println!("Poison wears off.");
            }
        }

        Spell {
            name: "Poison".to_string(),
            cost: 173,
            effect_or_instant: EffectOrInstant::Effect {
                timer: 6,
                in_turn,
                on,
                off,
            },
            total_heal: 0,
            total_mana: 0,
            total_armor: 0,
            total_damage: 6 * 3,
        }
    }

    pub fn recharge() -> Spell {
        fn in_turn(wizard: &mut Wizard, _warrior: &mut Warrior, print: bool, current_timer: usize) {
            wizard.mana_points += 101;
            if print {
                println!(
                    "Recharge provides 101 mana; its timer is now {}.",
                    current_timer
                );
            }
        }

        fn on(_wizard: &mut Wizard, _warrior: &mut Warrior, print: bool) {
            if print {
                println!("Player casts Recharge.");
            }
        }

        fn off(_wizard: &mut Wizard, _warrior: &mut Warrior, print: bool) {
            if print {
                println!("Recharge wears off.");
            }
        }

        Spell {
            name: "Recharge".to_string(),
            cost: 229,
            effect_or_instant: EffectOrInstant::Effect {
                timer: 5,
                in_turn,
                on,
                off,
            },
            total_heal: 0,
            total_mana: 101 * 5,
            total_armor: 0,
            total_damage: 0,
        }
    }

    pub fn decrease_timer(&mut self) {
        if let EffectOrInstant::Effect {
            timer,
            in_turn,
            on,
            off,
        } = self.effect_or_instant
        {
            self.effect_or_instant = EffectOrInstant::Effect {
                timer: timer.saturating_sub(1),
                in_turn,
                on,
                off,
            };
        }
    }
}

impl Display for Spell {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> Result<(), std::fmt::Error> {
        write!(f, "Spell({}, {})", self.name, self.cost)
    }
}

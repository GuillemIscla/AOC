use crate::year_2015::day_006::instruction::Instruction;
use crate::year_2015::day_006::light_action::LightAction;

#[derive(Debug, Clone, Hash, Eq, PartialEq)]
pub struct BrightGrid {
    raw: Vec<Vec<usize>>,
}

impl BrightGrid {
    pub fn new() -> BrightGrid {
        let max = 1000;
        BrightGrid {
            raw: vec![vec![0; max]; max],
        }
    }

    pub fn process(&mut self, instruction: Instruction) {
        let light_action_func: fn(usize) -> usize = match instruction.light_action {
            LightAction::TurnOn => |u: usize| -> usize { u + 1 },
            LightAction::TurnOff => |u: usize| -> usize {
                if u == 0 {
                    u
                } else {
                    u - 1
                }
            },
            LightAction::Toggle => |u: usize| -> usize { u + 2 },
        };

        for i in instruction.x_from..instruction.x_to + 1 {
            for j in instruction.y_from..instruction.y_to + 1 {
                self.raw[i][j] = light_action_func(self.raw[i][j]);
            }
        }
    }

    pub fn on_count(&self) -> usize {
        self.raw.iter().map(|row| row.iter().sum::<usize>()).sum()
    }

    #[allow(dead_code)]
    pub fn print(&self) {
        for row in &self.raw {
            for &cell in row {
                print!("{}", cell);
            }
            println!();
        }
    }
}

use crate::year_2015::day_006::instruction::Instruction;
use crate::year_2015::day_006::light_action::LightAction;

#[derive(Debug, Clone, Hash, Eq, PartialEq)]
pub struct LightGrid {
    raw: Vec<Vec<bool>>,
}

impl LightGrid {
    pub fn new() -> LightGrid {
        let max = 1000;
        LightGrid {
            raw: vec![vec![false; max]; max],
        }
    }

    pub fn process(&mut self, instruction: Instruction) {
        let light_action_func: fn(bool) -> bool = match instruction.light_action {
            LightAction::TurnOn => |_b: bool| -> bool { true },
            LightAction::TurnOff => |_b: bool| -> bool { false },
            LightAction::Toggle => |b: bool| -> bool { !b },
        };

        for i in instruction.x_from..instruction.x_to + 1 {
            for j in instruction.y_from..instruction.y_to + 1 {
                self.raw[i][j] = light_action_func(self.raw[i][j]);
            }
        }
    }

    pub fn on_count(&self) -> usize {
        self.raw
            .iter()
            .map(|row| row.iter().filter(|&&light| light).count())
            .sum()
    }

    #[allow(dead_code)]
    pub fn print(&self) {
        for row in &self.raw {
            for &cell in row {
                let symbol = if cell { '#' } else { '.' };
                print!("{}", symbol);
            }
            println!();
        }
    }
}

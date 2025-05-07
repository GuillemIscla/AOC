use super::program_instruction::ProgramInstruction;
use std::fmt::Display;

pub struct Program {
    instructions: Vec<ProgramInstruction>,
    a: usize,
    b: usize,
}

impl Program {
    pub fn new(instructions: Vec<ProgramInstruction>) -> Program {
        Program {
            instructions,
            a: 0,
            b: 0,
        }
    }

    pub fn run(&mut self) -> (usize, usize) {
        let mut index: isize = 0;

        while let Some(program_instruction) = usize::try_from(index)
            .ok()
            .and_then(|index| self.instructions.get(index))
        {
            program_instruction.process(&mut index, &mut self.a, &mut self.b);
        }

        (self.a, self.b)
    }

    pub fn set_registers(&mut self, a: usize, b: usize) {
        self.a = a;
        self.b = b;
    }
}

impl Display for Program {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> Result<(), std::fmt::Error> {
        write!(
            f,
            "Program(instructions: {:?}, a: {}, b: {})",
            self.instructions, self.a, self.b
        )
    }
}

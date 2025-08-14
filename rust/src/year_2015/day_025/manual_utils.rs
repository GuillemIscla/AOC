use std::fmt::{self, Display};

pub struct CodeInManual {
    pub code: usize,
    pub row: usize,
    pub column: usize,
}

impl CodeInManual {
    pub fn first_code() -> CodeInManual {
        CodeInManual {
            code: 20151125,
            row: 1,
            column: 1,
        }
    }

    pub fn next(&self) -> CodeInManual {
        let code = (self.code * 252533) % 33554393;
        let column = if self.row == 1 { 1 } else { self.column + 1 };
        let row = if column == 1 {
            self.column + 1
        } else {
            self.row - 1
        };
        CodeInManual { code, row, column }
    }
}

impl Display for CodeInManual {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(
            f,
            "CodeInManual(code: {}, row: {}, column: {})",
            self.code, self.row, self.column
        )
    }
}

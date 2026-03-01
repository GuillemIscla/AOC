pub struct RollsDiagram {
    pub raw: Vec<Vec<char>>,
}

impl RollsDiagram {
    pub fn new(s: String) -> RollsDiagram {
        let raw = s.split("\n").map(|line| line.chars().collect()).collect();

        RollsDiagram { raw }
    }

    pub fn rolls_adjacent(&self, i: usize, j: usize) -> i8 {
        if self.raw.is_empty() {
            return 0;
        }
        let mut count = 0;
        for x in -1isize..=1 {
            for y in -1isize..=1 {
                let is_same_cell = x == 0 && y == 0;
                if !is_same_cell &&
                    let Some(xi) = i.checked_add_signed(x) && 
                    let Some(yj) = j.checked_add_signed(y) && 
                        xi < self.raw.len() && 
                        yj < self.raw[xi].len() && 
                        self.raw[xi][yj] == '@' {
                    count += 1;
                }
            }
        }
        count
    }
}

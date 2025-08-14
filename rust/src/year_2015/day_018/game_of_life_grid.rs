use std::fmt::{self, Display};

pub struct GameOrLifeGrid {
    raw: Vec<Vec<bool>>,
    corners_stuck: bool,
}

impl GameOrLifeGrid {
    pub fn new(raw: Vec<Vec<bool>>, corners_stuck: bool) -> GameOrLifeGrid {
        GameOrLifeGrid { raw, corners_stuck }
    }

    pub fn lights_on(&self) -> usize {
        let mut usize = 0;
        for i in 0..self.raw.len() {
            for j in 0..self.raw.len() {
                if self.cell_value(i, j) {
                    usize += 1;
                }
            }
        }
        usize
    }

    pub fn next(&self) -> GameOrLifeGrid {
        let mut raw: Vec<Vec<bool>> = Vec::new();
        for i in 0..self.raw.len() {
            let mut column: Vec<bool> = Vec::new();
            for j in 0..self.raw.len() {
                column.push(self.next_cell(i, j));
            }
            raw.push(column);
        }
        GameOrLifeGrid {
            raw,
            corners_stuck: self.corners_stuck,
        }
    }

    fn cell_value(&self, i: usize, j: usize) -> bool {
        if self.corners_stuck
            && (i == 0 || i == self.raw.len() - 1)
            && (j == 0 || j == self.raw.len() - 1)
        {
            return true;
        }
        match self.raw.get(i) {
            Some(column) => *column.get(j).unwrap_or(&false),
            None => false,
        }
    }

    fn next_cell(&self, i: usize, j: usize) -> bool {
        let neighbour_coordinates: Vec<(isize, isize)> = vec![
            (1, 1),
            (1, 0),
            (1, -1),
            (0, 1),
            (0, -1),
            (-1, 1),
            (-1, 0),
            (-1, -1),
        ];
        let neighbours_alive = neighbour_coordinates
            .into_iter()
            .filter(|(a, b)| {
                let ia = i as isize + a;
                let jb = j as isize + b;
                if ia >= 0 && jb >= 0 {
                    self.cell_value(ia as usize, jb as usize)
                } else {
                    false
                }
            })
            .count();
        if self.cell_value(i, j) {
            neighbours_alive == 2 || neighbours_alive == 3
        } else {
            neighbours_alive == 3
        }
    }
}

impl Display for GameOrLifeGrid {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        writeln!(f)?;
        for i in 0..self.raw.len() {
            for j in 0..self.raw.len() {
                let char = if self.raw[i][j] { "#" } else { "." };
                write!(f, "{}", char)?;
            }
            writeln!(f)?;
        }
        Ok(())
    }
}

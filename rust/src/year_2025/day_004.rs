use parse::*;

mod parse;
mod rolls_diagram;

#[allow(dead_code)]
pub fn part_1() {
    let rolls_diagram = parse(None);
    let mut count = 0;
    for i in 0..rolls_diagram.raw.len() {
        for j in 0..rolls_diagram.raw[i].len() {
            if rolls_diagram.raw[i][j] == '@' && rolls_diagram.rolls_adjacent(i, j) < 4 {
                count += 1;
            }
        }
    }

    println!("Part 1: {}", count)
}

#[allow(dead_code)]
pub fn part_2() {
    let mut rolls_diagram = parse(None);
    let mut count = 0;
    let mut will_try_access = true;
    while will_try_access {
        let mut count_this_time = 0;
        let mut accessed: Vec<(usize, usize)> = Vec::new();
        for i in 0..rolls_diagram.raw.len() {
            for j in 0..rolls_diagram.raw[i].len() {
                if rolls_diagram.raw[i][j] == '@' && rolls_diagram.rolls_adjacent(i, j) < 4 {
                    accessed.push((i, j));
                    count_this_time += 1;
                }
            }
        }

        for pair in accessed {
            rolls_diagram.raw[pair.0][pair.1] = '.';
        }
        count += count_this_time;

        will_try_access = count_this_time > 0;
    }

    println!("Part 2: {}", count)
}

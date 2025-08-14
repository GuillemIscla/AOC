use parse::*;

mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let input = parse();
    let mut floor = 0;

    for c in input.chars() {
        if c == '(' {
            floor += 1;
        } else {
            floor -= 1;
        }
    }

    println!("Part 1: {}", floor);
}

#[allow(dead_code)]
pub fn part_2() {
    let input = parse();
    let mut floor = 0;
    let mut steps = 0;

    for c in input.chars() {
        if c == '(' {
            floor += 1;
        } else {
            floor -= 1;
        }
        steps += 1;
        if floor == -1 {
            break;
        }
    }

    println!("Part 2: {}", steps);
}

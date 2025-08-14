mod parse;
mod present;

#[allow(dead_code)]
pub fn part_1() {
    let input = parse::parse(true);

    let mut total_required_paper = 0;
    let mut small_side_a;
    let mut small_side_b;
    for present in input.iter() {
        (small_side_a, small_side_b) = present.smaller_sides();
        total_required_paper += present.required_paper() + small_side_a * small_side_b;
    }
    println!("Part 1: {}", total_required_paper);
}

#[allow(dead_code)]
pub fn part_2() {
    let input = parse::parse(true);

    let mut total_required_ribbon = 0;
    let mut small_side_a;
    let mut small_side_b;
    for present in input.iter() {
        (small_side_a, small_side_b) = present.smaller_sides();
        total_required_ribbon +=
            2 * small_side_a + 2 * small_side_b + present.l * present.w * present.h;
    }
    println!("Part 2: {}", total_required_ribbon);
}

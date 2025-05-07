use crate::year_2015::day_005::parse::parse;
mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let input = parse(0);
    let nice_strings = input
        .iter()
        .filter(|s| condition1(s) && condition2(s) && condition3(s));
    println!("Part 1: {}", nice_strings.count());
}

pub fn condition1(s: &str) -> bool {
    let vowels = "aeiou";
    s.chars().filter(|c| vowels.contains(|v| v == *c)).count() >= 3
}

pub fn condition2(s: &str) -> bool {
    let mut is_found = false;
    let mut before = 'c';
    for (i, c) in s.chars().enumerate() {
        if i == 0 {
            before = c;
        } else {
            is_found = c == before;
            before = c;
        }
        if is_found {
            break;
        }
    }
    is_found
}

pub fn condition3(s: &str) -> bool {
    let not_allowed = [
        String::from("ab"),
        String::from("cd"),
        String::from("pq"),
        String::from("xy"),
    ];

    not_allowed.iter().find(|na| s.contains(*na)).iter().count() == 0
}

#[allow(dead_code)]
pub fn part_2() {
    let input = parse(0);
    let nice_strings = input.iter().filter(|s| condition4(s) && condition5(s));
    println!("Part 2: {:?}", nice_strings.count());
}

pub fn condition4(s: &str) -> bool {
    for (i, _c) in s.chars().enumerate() {
        if i < s.len() - 2 {
            let pair = &s[i..i + 2];
            let slice_rest = String::from(&s[i + 2..]);
            if slice_rest.contains(pair) {
                return true;
            }
        }
    }
    false
}

pub fn condition5(s: &str) -> bool {
    for (i, c) in s.chars().enumerate() {
        if Some(c) == s.chars().nth(i + 2) {
            return true;
        }
    }
    false
}

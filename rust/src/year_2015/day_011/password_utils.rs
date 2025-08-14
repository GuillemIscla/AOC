pub fn next_password(old: String) -> String {
    let mut i = old.len() - 1;
    let (mut overflow, mut new_char) = increment_char(old.as_bytes()[i] as char);
    let mut new_tail = String::from(new_char);

    while overflow {
        i -= 1;
        (overflow, new_char) = increment_char(old.as_bytes()[i] as char);
        new_tail.insert(0, new_char);
    }

    let mut old_sliced = String::from(&old[0..i]);
    old_sliced.push_str(&new_tail);
    old_sliced
}

pub fn increment_char(c: char) -> (bool, char) {
    if c == 'z' {
        (true, 'a')
    } else {
        let next_char = std::char::from_u32(c as u32 + 1).unwrap();
        (false, next_char)
    }
}

pub fn security_1(password: &String) -> bool {
    let mut i = 0;
    while i < password.len() - 2 {
        let first = password.as_bytes()[i] as u32;
        let second = password.as_bytes()[i + 1] as u32;
        let third = password.as_bytes()[i + 2] as u32;
        if first + 1 == second && second + 1 == third {
            return true;
        }
        i += 1;
    }
    false
}

pub fn security_2(password: &str) -> bool {
    !password.chars().any(|c| c == 'i')
        && !password.chars().any(|c| c == 'o')
        && !password.chars().any(|c| c == 'l')
}

pub fn security_3(password: &String) -> bool {
    let mut i = 0;
    let mut overlapping_count = 0;
    while i < password.len() - 1 {
        let first = password.as_bytes()[i] as u32;
        let second = password.as_bytes()[i + 1] as u32;
        if first == second {
            overlapping_count += 1;
            i += 1;
        }
        if overlapping_count == 2 {
            return true;
        }
        i += 1;
    }
    false
}

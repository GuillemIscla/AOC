use std::collections::{HashMap, VecDeque};
use crate::current_day::measured_pair::MeasuredPair;
use itertools::Itertools;
use parse::*;

mod junction_box;
mod measured_pair;
mod parse;

#[allow(dead_code)]
pub fn part_1() {
    let (connections_size, junction_boxes) = parse(None);

    let mut measured_pairs: Vec<MeasuredPair> = Vec::new();

    //Making the complete graph
    for (i, junction_box_1) in junction_boxes.iter().enumerate() {
        for (j, junction_box_2) in junction_boxes.iter().enumerate() {
            if j > i {
                measured_pairs.push(MeasuredPair {
                    one: i,
                    two: j,
                    dist_square: junction_box_1.distance_square(junction_box_2.clone()),
                })
            }
        }
    }

    //Adding the first connections
    measured_pairs.sort_by_key(|mp| mp.dist_square);
    measured_pairs = measured_pairs.drain(..connections_size).collect();

    let groups: Vec<Vec<usize>> = connectivity(measured_pairs);

    let result: usize = 
        groups
            .into_iter()
            .map(|group| group.len())
            .sorted_by(|a,b|b.cmp(a))
            .take(3)
            .product();

    println!("Part 1: {}", result);
}

#[allow(dead_code)]
pub fn part_2() {
    let (_, junction_boxes) = parse(None);

    let mut measured_pairs: Vec<MeasuredPair> = Vec::new();

    //Making the complete graph
    for (i, junction_box_1) in junction_boxes.iter().enumerate() {
        for (j, junction_box_2) in junction_boxes.iter().enumerate() {
            if j > i {
                measured_pairs.push(MeasuredPair {
                    one: i,
                    two: j,
                    dist_square: junction_box_1.distance_square(junction_box_2.clone()),
                })
            }
        }
    }

    //Finding out the first moment where all the nodes are mentioned
    measured_pairs.sort_by_key(|mp| mp.dist_square);
    let mut when_added:HashMap<usize, usize> = HashMap::new();
    for i in 0..junction_boxes.len() {
        let (index, _) = measured_pairs.iter().enumerate().find(|(_, mp)|mp.contains(i)).unwrap();
        when_added.insert(i, index);
    }
    let when_mentioned_last = *when_added.values().max().unwrap();

    //Dividing the connections into already added and added
    let (added_connections_raw, possible_connections_raw) = measured_pairs.split_at(when_mentioned_last);
    let mut added_connections = added_connections_raw.to_vec();
    let mut possible_connections = VecDeque::from(possible_connections_raw.to_vec());

    //Iterate in case they are all added but there is no connectivity
    let mut groups = connectivity(added_connections.clone());
    loop {
        match groups.as_slice() {
            [one_group] if one_group.len() == junction_boxes.len() => break,
            _ => {
                if let Some(new_added_connection) = possible_connections.pop_front() {
                    added_connections.push(new_added_connection);
                    groups = connectivity(added_connections.clone());
                }
            },
        }
    }

    let last_added = added_connections.last().unwrap();
    let result = junction_boxes[last_added.one].0 * junction_boxes[last_added.two].0;
    
    println!("Part 2: {}", result);
}


fn connectivity(mut connections: Vec<MeasuredPair>) -> Vec<Vec<usize>> {
    let mut groups: Vec<Vec<usize>> = Vec::new();
    while !connections.is_empty() {
        if let Some(MeasuredPair {
            one,
            two,
            dist_square: _,
        }) = connections.pop()
        {
            let mut not_visited = vec![one, two];
            let mut visited: Vec<usize> = Vec::new();
            while !not_visited.is_empty() {
                if let Some(not_visited_index) = not_visited.pop() && !visited.contains(&not_visited_index) {
                    let (removed, kept): (Vec<MeasuredPair>, Vec<MeasuredPair>) = 
                            connections.iter().partition(|connection| connection.contains(not_visited_index));
                    removed.into_iter().for_each(|MeasuredPair { one, two, dist_square:_ }| {
                        if !not_visited.contains(&one) && !visited.contains(&one) {
                            not_visited.push(one);
                        }
                        if !not_visited.contains(&two) && !visited.contains(&two) {
                            not_visited.push(two);
                        }
                    });
                    visited.push(not_visited_index);
                    connections = kept;
                }
            }
            groups.push(visited)
        }
    }
    
    groups
}

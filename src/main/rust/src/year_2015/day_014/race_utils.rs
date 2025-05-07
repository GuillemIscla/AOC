use std::cmp;
use std::collections::HashMap;

use super::reindeer::Reindeer;

pub fn run_race_segment(
    segment_start: usize,
    points: &mut HashMap<String, usize>,
    reindeers: &[Reindeer],
    race_end: usize,
) -> usize {
    let leads = get_leads(reindeers, segment_start);
    let first_lead = leads.first().expect("No leads is not possible");
    let leads_speeds = leads
        .iter()
        .map(|reindeer| reindeer.speed_at_time(segment_start))
        .collect::<Vec<usize>>();

    let leads_max_speed = leads_speeds.iter().max().expect("No leads is not possible");

    let leads_have_different_speeds = leads
        .iter()
        .any(|reindeer| &reindeer.speed_at_time(segment_start) != leads_max_speed);

    let segment_end = if leads_have_different_speeds {
        segment_start + 1
    } else {
        let lead_stop = leads
            .iter()
            .map(|lead| lead.next_speed_change(segment_start, false))
            .min()
            .unwrap_or(0);
        let non_leads = reindeers
            .iter()
            .filter(|reindeer| !leads.iter().any(|lead| lead.name == reindeer.name))
            .collect::<Vec<&Reindeer>>();
        let leads_catch_up_time = non_leads
            .into_iter()
            .flat_map(|non_lead| catch_up_if_same_speed(first_lead, non_lead, segment_start))
            .min();

        match leads_catch_up_time {
            Some(leads_catch_up_time) => cmp::min(lead_stop, leads_catch_up_time),
            None => lead_stop,
        }
    };

    let segment_end_race_end = cmp::min(segment_end, race_end + 1);
    let points_in_segment = segment_end_race_end
        .checked_sub(segment_start)
        .unwrap_or_else(|| {
            panic!(
                "Start is after end, {}, {}",
                segment_start, segment_end_race_end
            )
        });
    leads.iter().for_each(|lead| {
        match points.get(&lead.name) {
            Some(old_points) => {
                points.insert(lead.name.to_string(), old_points + points_in_segment)
            }
            None => points.insert(lead.name.to_string(), points_in_segment),
        };
    });

    segment_end_race_end
}

pub fn get_leads(reindeers: &[Reindeer], time: usize) -> Vec<&Reindeer> {
    let reindeers_and_distance: Vec<(&Reindeer, usize)> = reindeers
        .iter()
        .map(|reindeer| (reindeer, reindeer.distance_in_race(time)))
        .collect();

    let lead_position = reindeers_and_distance
        .iter()
        .max_by(|a, b| a.1.cmp(&b.1))
        .unwrap()
        .1;

    reindeers_and_distance
        .into_iter()
        .filter(|(_, distance)| *distance == lead_position)
        .map(|(reindeer, _)| reindeer)
        .collect::<Vec<&Reindeer>>()
}

fn catch_up_if_same_speed(
    reindeer_a: &Reindeer,
    reindeer_b: &Reindeer,
    time: usize,
) -> Option<usize> {
    let reindeer_a_pos = reindeer_a.distance_in_race(time) as isize;
    let reindeer_b_pos = reindeer_b.distance_in_race(time) as isize;
    let reindeer_a_speed = reindeer_a.speed_at_time(time) as isize;
    let reindeer_b_speed = reindeer_b.speed_at_time(time) as isize;
    if reindeer_a_speed == reindeer_b_speed && reindeer_b_speed > 0 {
        None
    } else if reindeer_b_speed > 0 {
        let mut catch_in_time =
            (reindeer_b_pos - reindeer_a_pos) / (reindeer_a_speed - reindeer_b_speed);

        //If at the end of the second they are at the same position, we want to process that very
        //instant. If is not exact, means that if we don't add +1 actually reinder_b will still be behind.
        let is_exact = catch_in_time * (reindeer_a_speed - reindeer_b_speed)
            == (reindeer_b_pos - reindeer_a_pos);

        if !is_exact {
            catch_in_time += 1;
        }

        catch_in_time
            .try_into()
            .ok()
            .filter(|result| *result != 0)
            .map(|catch_in_time: usize| time + catch_in_time)
    } else {
        catch_up_if_same_speed(
            reindeer_a,
            reindeer_b,
            reindeer_b.next_speed_change(time, true),
        )
    }
}

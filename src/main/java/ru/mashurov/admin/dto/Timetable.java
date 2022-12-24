package ru.mashurov.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Timetable {

	private List<TimePeriod> monday;

	private List<TimePeriod> tuesday;

	private List<TimePeriod> wednesday;

	private List<TimePeriod> thursday;

	private List<TimePeriod> friday;
}

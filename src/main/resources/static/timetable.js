const DAYS = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday']

function initEvents() {

    $('.btn-add-time').click(function () {

        const day = $(this).attr('id').split("_")[1]

        const timeStart = $(this).closest('.input-group').children('.time-start').val()
        const timeEnd = $(this).closest('.input-group').children('.time-end').val()

        if (timeStart !== '' && timeEnd !== '') {

            if ($('#' + day + '_times').children('.list-group-item + .list-group-item-action').text() === 'Нет расписания') {
                $('#' + day + '_times').children('.list-group-item + .list-group-item-action').remove()
            }

            $('#' + day + '_times').append($("<a>")
                .text(timeStart + ':00' + ' - ' + timeEnd + ':00')
                .addClass('list-group-item list-group-item-action'))
        }
    })

    $('.list-group-item.list-group-item-action').dblclick(function () {
        $(this).remove()
    })

    $('#table-save-btn').click(function () {

        let timetable = new Map()

        DAYS.forEach(function (day) {

            const dayRed = day.slice(0, 3)
            let timePeriods = []

            $('#' + dayRed + '_times').children('.list-group-item + .list-group-item-action').each(function () {

                if ($(this).text() !== 'Нет расписания') {

                    const startEnd = $(this).text().split(' - ')
                    const start = startEnd[0]
                    const end = startEnd[1]

                    const period = {
                        "start": start,
                        "end": end
                    }

                    timePeriods.push(period)
                }
            })

            timetable.set(day, timePeriods)
        })

        const data = {
            "veterinarianId": $('#vetId').attr('value'),
            "id": $('#timetableId').attr('value'),
            "timetable": Object.fromEntries(timetable)
        }

        $.ajax(
            '/major/veterinarians/' + data.veterinarianId + '/change_timetable',
            {
                type: 'POST',
                data: JSON.stringify(data),
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                async: false,
                success: function (data) {
                    window.location = '/major/veterinarians/' + data + '/change_timetable'
                }
            },
        )
    })
}

initEvents()
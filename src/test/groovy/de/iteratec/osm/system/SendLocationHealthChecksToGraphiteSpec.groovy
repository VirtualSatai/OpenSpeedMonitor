package de.iteratec.osm.system

import de.iteratec.osm.ConfigService
import de.iteratec.osm.batch.BatchActivityService
import de.iteratec.osm.measurement.environment.Location
import de.iteratec.osm.measurement.environment.WebPageTestServer
import de.iteratec.osm.report.external.GraphiteServer
import de.iteratec.osm.report.external.MockedGraphiteSocket
import de.iteratec.osm.report.external.provider.DefaultGraphiteSocketProvider
import grails.buildtestdata.BuildDataTest
import grails.buildtestdata.mixin.Build
import grails.testing.services.ServiceUnitTest
import org.joda.time.DateTime
import spock.lang.Specification

@Build([Location, LocationHealthCheck, WebPageTestServer, GraphiteServer])
class SendLocationHealthChecksToGraphiteSpec extends Specification implements BuildDataTest,
        ServiceUnitTest<LocationHealthCheckService> {

    public static final String GRAPHITESERVERS_HEALTH_PREFIX = "osm.health"
    public static final String WPTSERVERS_LABEL = "mywptserver.com"
    public static final String LOCATIONS_IDENTIFIER = "mylocationidentifier"

    public MockedGraphiteSocket graphiteSocketUsedInTests = new MockedGraphiteSocket()

    def setup() {
        mockGraphiteSocketProvider()
    }

    void setupSpec() {
        mockDomains(LocationHealthCheck, GraphiteServer)
    }

    Closure doWithSpring() {
        return {
            configService(ConfigService)
            batchActivityService(BatchActivityService)
        }
    }

    void "reportToGraphiteServers sends no LocationHealthChecks to graphite if no graphite server exists"(){
        given: "creating a LocationHealthCheck"
        List<LocationHealthCheck> healthChecks = [
                LocationHealthCheck.build(
                    save: false,
                    date: new DateTime().toDate(),
                    numberOfAgents: 2,
                    numberOfPendingJobsInWpt: 12,
                    numberOfJobResultsLastHour: 14,
                    numberOfEventResultsLastHour: 120,
                    numberOfErrorsLastHour: 2,
                    numberOfJobResultsNextHour: 16,
                    numberOfEventResultsNextHour: 140,
                    numberOfCurrentlyPendingJobs: 20,
                    numberOfCurrentlyRunningJobs: 4,
                    location: Location.build(save: false,
                        uniqueIdentifierForServer: LOCATIONS_IDENTIFIER,
                        wptServer: WebPageTestServer.build(save: false,label: WPTSERVERS_LABEL)
                    )
                )
        ]

        when: "reportToGraphiteServers is called for the LocationHealthCheck"
        service.reportToGraphiteServers(healthChecks)

        then: "nothing would have been sent to graphite"
        graphiteSocketUsedInTests.sentDates.size() == 0

    }
    void "reportToGraphiteServers sends no LocationHealthChecks to graphite if no graphite server with reportHealthMetrics = true exists"(){
        given: "creating a LocationHealthCheck and a graphite server with reportHealthMetrics = false"
        GraphiteServer.build(reportHealthMetrics: false)
        List<LocationHealthCheck> healthChecks = [
                LocationHealthCheck.build(
                    save: false,
                    date: new DateTime().toDate(),
                    numberOfAgents: 2,
                    numberOfPendingJobsInWpt: 12,
                    numberOfJobResultsLastHour: 14,
                    numberOfEventResultsLastHour: 120,
                    numberOfErrorsLastHour: 2,
                    numberOfJobResultsNextHour: 16,
                    numberOfEventResultsNextHour: 140,
                    numberOfCurrentlyPendingJobs: 20,
                    numberOfCurrentlyRunningJobs: 4,
                    location: Location.build(
                            save: false,
                            uniqueIdentifierForServer: LOCATIONS_IDENTIFIER,
                            wptServer: WebPageTestServer.build(save: false, label: WPTSERVERS_LABEL)
                    )
                )
        ]

        when: "reportToGraphiteServers is called for the LocationHealthCheck"
        service.reportToGraphiteServers(healthChecks)

        then: "nothing would have been sent to graphite"
        graphiteSocketUsedInTests.sentDates.size() == 0

    }

    void "reportToGraphiteServers sends single LocationHealthCheck to graphite correctly if a graphite server with reportHealthMetrics = true exists"(){
        given: "creating a LocationHealthCheck and a graphite server with reportHealthMetrics = true"
        GraphiteServer.build(healthMetricsReportPrefix: GRAPHITESERVERS_HEALTH_PREFIX, reportHealthMetrics: true)
        List<LocationHealthCheck> healthChecks = [
                LocationHealthCheck.build(
                    save: false,
                    date: new DateTime().toDate(),
                    numberOfAgents: 2,
                    numberOfPendingJobsInWpt: 12,
                    numberOfJobResultsLastHour: 14,
                    numberOfEventResultsLastHour: 120,
                    numberOfErrorsLastHour: 2,
                    numberOfJobResultsNextHour: 16,
                    numberOfEventResultsNextHour: 140,
                    numberOfCurrentlyPendingJobs: 20,
                    numberOfCurrentlyRunningJobs: 4,
                    location: Location.build(save: false,
                            uniqueIdentifierForServer: LOCATIONS_IDENTIFIER,
                            wptServer: WebPageTestServer.build(save: false,label: WPTSERVERS_LABEL)
                    )
                )
        ]
        String expectedGraphitePathPrefix = "${GRAPHITESERVERS_HEALTH_PREFIX}.${WPTSERVERS_LABEL}.${LOCATIONS_IDENTIFIER}"

        when: "reportToGraphiteServers is called for the LocationHealthCheck"
        service.reportToGraphiteServers(healthChecks)
        List<String> pathesThatWasSent = graphiteSocketUsedInTests.sentDates*.path*.toString()

        then: "every graphite relevant healthcheck attribute has been sent to graphite server"
        graphiteSocketUsedInTests.sentDates.size() == service.HEALTHCHECK_ATTRIBUTES_TO_SEND.size()
        service.HEALTHCHECK_ATTRIBUTES_TO_SEND.each { String attributeThatShouldHaveBeenSent ->
            String graphitePathThatShouldHaveBeenSent = "${expectedGraphitePathPrefix}.${attributeThatShouldHaveBeenSent}"
            assert pathesThatWasSent.contains(graphitePathThatShouldHaveBeenSent)
        }

    }

    void "reportToGraphiteServers sends multiple LocationHealthChecks to graphite correctly if a graphite server with reportHealthMetrics = true exists"(){
        given: "creating a LocationHealthCheck and a graphite server with reportHealthMetrics = true"
        GraphiteServer.build(healthMetricsReportPrefix: GRAPHITESERVERS_HEALTH_PREFIX, reportHealthMetrics: true)
        List<LocationHealthCheck> healthChecks = [
                LocationHealthCheck.build(
                    save: false,
                    date: new DateTime().toDate(),
                    numberOfAgents: 2,
                    numberOfPendingJobsInWpt: 12,
                    numberOfJobResultsLastHour: 14,
                    numberOfEventResultsLastHour: 120,
                    numberOfErrorsLastHour: 2,
                    numberOfJobResultsNextHour: 16,
                    numberOfEventResultsNextHour: 140,
                    numberOfCurrentlyPendingJobs: 20,
                    numberOfCurrentlyRunningJobs: 4,
                    location: Location.build(
                            save: false,
                            uniqueIdentifierForServer: LOCATIONS_IDENTIFIER,
                            wptServer: WebPageTestServer.build(save: false, label: WPTSERVERS_LABEL)
                    )
                ),
                LocationHealthCheck.build(
                    save: false,
                    date: new DateTime().toDate(),
                    numberOfAgents: 2,
                    numberOfPendingJobsInWpt: 12,
                    numberOfJobResultsLastHour: 14,
                    numberOfEventResultsLastHour: 120,
                    numberOfErrorsLastHour: 2,
                    numberOfJobResultsNextHour: 16,
                    numberOfEventResultsNextHour: 140,
                    numberOfCurrentlyPendingJobs: 20,
                    numberOfCurrentlyRunningJobs: 4,
                    location: Location.build(
                            save: false,
                            uniqueIdentifierForServer: LOCATIONS_IDENTIFIER,
                            wptServer: WebPageTestServer.build(save: false, label: WPTSERVERS_LABEL)
                    )
                ),
                LocationHealthCheck.build(
                    save: false,
                    date: new DateTime().toDate(),
                    numberOfAgents: 2,
                    numberOfPendingJobsInWpt: 12,
                    numberOfJobResultsLastHour: 14,
                    numberOfEventResultsLastHour: 120,
                    numberOfErrorsLastHour: 2,
                    numberOfJobResultsNextHour: 16,
                    numberOfEventResultsNextHour: 140,
                    numberOfCurrentlyPendingJobs: 20,
                    numberOfCurrentlyRunningJobs: 4,
                    location: Location.build(
                            save: false,
                            uniqueIdentifierForServer: LOCATIONS_IDENTIFIER,
                            wptServer: WebPageTestServer.build(save: false, label: WPTSERVERS_LABEL)
                    )
                )
        ]
        String expectedGraphitePathPrefix = "${GRAPHITESERVERS_HEALTH_PREFIX}.${WPTSERVERS_LABEL}.${LOCATIONS_IDENTIFIER}"

        when: "reportToGraphiteServers is called for the LocationHealthCheck"
        service.reportToGraphiteServers(healthChecks)
        List<String> pathesThatWasSent = graphiteSocketUsedInTests.sentDates*.path*.toString()

        then: "every graphite relevant healthcheck attribute has been sent to graphite server"
        graphiteSocketUsedInTests.sentDates.size() == healthChecks.size() * service.HEALTHCHECK_ATTRIBUTES_TO_SEND.size()
        service.HEALTHCHECK_ATTRIBUTES_TO_SEND.each { String attributeThatShouldHaveBeenSent ->
            String graphitePathThatShouldHaveBeenSent = "${expectedGraphitePathPrefix}.${attributeThatShouldHaveBeenSent}"
            int countThisAttributeHasBeenSent = pathesThatWasSent.findAll { it == graphitePathThatShouldHaveBeenSent }.size()
            assert countThisAttributeHasBeenSent == healthChecks.size()
        }

    }

    /**
     * Mocks {@linkplain de.iteratec.osm.report.external.provider.GraphiteSocketProvider#getSocket}.
     */
    private void mockGraphiteSocketProvider() {
        service.graphiteSocketProvider = Stub(DefaultGraphiteSocketProvider)
        service.graphiteSocketProvider.getSocket(_) >> graphiteSocketUsedInTests
    }
}
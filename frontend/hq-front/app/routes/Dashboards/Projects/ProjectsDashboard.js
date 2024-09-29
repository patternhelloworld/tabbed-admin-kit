import React from "react";
import { faker } from "@faker-js/faker";
import { Link } from "react-router-dom";
import {
  Container,
  Row,

  CardBody,

  Table,
  CardTitle,
  Button,
  InputGroup,
  InputGroupAddon,
  Input,
  ListGroup,
  ListGroupItem,
  Media,
  Col,
} from "./../../../components";

import { Card, Text, Title, Badge, Group } from '@mantine/core';
import { setupPage } from "./../../../components/Layout/setupPage";

import { HeaderMain } from "../../../components/HeaderMain";

import { TasksMedia } from "../../../components/ProjectsDashboards/TasksMedia";
import { TinyDonutChart } from "../../../components/ProjectsDashboards/TinyDonutChart";
import { TinyDonutChartAllProjects } from "../../../components/ProjectsDashboards/TinyDonutChartAllProjects";
import { TimelineMini } from "../../../components/Timeline/TimelineMini";
import { DraggableProjects } from "./DraggableProjects";

const ProjectsDashboard = () => (
    <Container>
      <Row className="mb-5">
        <Col lg={12}>
          <Group direction="column" spacing="lg">
            <Card shadow="sm" padding="lg" radius="md" withBorder>
              <Title order={4}>Project Overview</Title>
              <Text>
                Using an open-source boilerplate:{' '}
                <a href="https://github.com/0wczar/airframe-react" target="_blank" rel="noopener noreferrer">
                  Airframe React
                </a>
                {' '}and Mantine Table, eliminating the need for designer involvement.
              </Text>
              <Badge color="blue" variant="light">
                React 18, Spring Boot 3.3
              </Badge>
            </Card>

            <Card shadow="sm" padding="lg" radius="md" withBorder>
              <Title order={4}>State Management</Title>
              <Text>
                Adopted Recoil for global state management in React due to the requirement for a tab-based UX on each menu click. Previously handled with CSS display, which caused performance issues.
              </Text>
            </Card>

            <Card shadow="sm" padding="lg" radius="md" withBorder>
              <Title order={4}>Performance Optimization</Title>
              <Text>
                Used <code>useCallback</code> in parent components and <code>useMemo</code> in child components to minimize unnecessary re-renders.
              </Text>
            </Card>

            <Card shadow="sm" padding="lg" radius="md" withBorder>
              <Title order={4}>Dockerization</Title>
              <Text>
                Dockerized using another open-source project:{' '}
                <a href="https://github.com/patternknife/docker-blue-green-runner" target="_blank" rel="noopener noreferrer">
                  Docker Blue-Green Runner
                </a>
              </Text>
            </Card>
          </Group>
        </Col>
      </Row>
    </Container>
);

export default setupPage({
  pageTitle: "Projects Dashboard",
})(ProjectsDashboard);

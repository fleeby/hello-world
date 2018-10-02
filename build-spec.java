import com.atlassian.bamboo.specs.api.BambooSpec;
import com.atlassian.bamboo.specs.api.builders.BambooKey;
import com.atlassian.bamboo.specs.api.builders.BambooOid;
import com.atlassian.bamboo.specs.api.builders.docker.DockerConfiguration;
import com.atlassian.bamboo.specs.api.builders.permission.PermissionType;
import com.atlassian.bamboo.specs.api.builders.permission.Permissions;
import com.atlassian.bamboo.specs.api.builders.permission.PlanPermissions;
import com.atlassian.bamboo.specs.api.builders.plan.Job;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;
import com.atlassian.bamboo.specs.api.builders.plan.PlanIdentifier;
import com.atlassian.bamboo.specs.api.builders.plan.Stage;
import com.atlassian.bamboo.specs.api.builders.plan.branches.BranchCleanup;
import com.atlassian.bamboo.specs.api.builders.plan.branches.PlanBranchManagement;
import com.atlassian.bamboo.specs.api.builders.plan.configuration.AllOtherPluginsConfiguration;
import com.atlassian.bamboo.specs.api.builders.plan.configuration.ConcurrentBuilds;
import com.atlassian.bamboo.specs.api.builders.project.Paaaaaaaaaaaaaaaaaaaaaaaroject;
import com.atlassian.bamboo.specs.builders.task.ScriptTask;
import com.atlassian.bamboo.specs.util.BambooServer;
import com.atlassian.bamboo.specs.util.MapBuilder;

@BambooSpec
public class PlanSpec {
    
    public Plan plan() {
        final Plan plan = new Plan(new Project()
                .oid(new BambooOid("613o8i3r9sld"))
                .key(new BambooKey("CON"))
                .name("ContainerBuild")
                .description("Build Containers"),
            "test-container-build",
            new BambooKey("TES"))
            .oid(new BambooOid("60tz0wqjg0zl"))
            .pluginConfigurations(new ConcurrentBuilds()
                    .useSystemWideDefault(false))
            .stages(new Stage("Default Stage")
                    .jobs(new Job("pwd job",
                            new BambooKey("JOB1"))
                            .pluginConfigurations(new AllOtherPluginsConfiguration()
                                    .configuration(new MapBuilder()
                                            .put("custom.isolated.docker", new MapBuilder()
                                                .put("extraContainers", "[]")
                                                .put("image", "centos")
                                                .put("imageSize", "XSMALL")
                                                .put("enabled", "true")
                                                .build())
                                            .build()))
                            .tasks(new ScriptTask()
                                    .description("pwd")
                                    .inlineBody("pwd\ndate\necho \"hi\""))
                            .dockerConfiguration(new DockerConfiguration()
                                    .enabled(false))))
            .planBranchManagement(new PlanBranchManagement()
                    .delete(new BranchCleanup())
                    .notificationForCommitters());
        return plan;
    }
    
    public PlanPermissions planPermission() {
        final PlanPermissions planPermission = new PlanPermissions(new PlanIdentifier("CON", "TES"))
            .permissions(new Permissions()
                    .userPermissions("root", PermissionType.EDIT, PermissionType.VIEW, PermissionType.ADMIN, PermissionType.CLONE, PermissionType.BUILD)
                    .loggedInUserPermissions(PermissionType.VIEW)
                    .anonymousUserPermissionView());
        return planPermission;
    }
    
    public static void main(String... argv) {
        //By default credentials are read from the '.credentials' file.
        BambooServer bambooServer = new BambooServer("http://34.218.229.145:8085");
        final PlanSpec planSpec = new PlanSpec();
        
        final Plan plan = planSpec.plan();
        bambooServer.publish(plan);
        
        final PlanPermissions planPermission = planSpec.planPermission();
        bambooServer.publish(planPermission);
    }
}

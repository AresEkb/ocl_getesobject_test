package ocl_getesobject_test;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ocl.pivot.ExpressionInOCL;
import org.eclipse.ocl.pivot.utilities.OCL;
import org.eclipse.ocl.xtext.essentialocl.EssentialOCLStandaloneSetup;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.UMLPackage;

public class Main {

    public static void main(String[] args) {
        final String input = "model/test.uml";

        ResourceSet rs = new ResourceSetImpl();
        EssentialOCLStandaloneSetup.doSetup();
        org.eclipse.ocl.pivot.uml.UMLStandaloneSetup.init();
        OCL ocl = OCL.newInstance(rs);

        Resource resource = ocl.getResourceSet().getResource(createFileURI(input), true);

        Model uml = (Model)EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.eINSTANCE.getModel());
        try {
            Class person = (Class)uml.getPackagedElement("Person");
            org.eclipse.ocl.pivot.Class personAS = ocl.getMetamodelManager().getASOf(org.eclipse.ocl.pivot.Class.class, person);
            System.out.println("PersonAS: " + personAS);

            org.eclipse.ocl.pivot.Property nameAttrAS = personAS.getOwnedProperties().get(0);

            // I get UMLPrimitiveType::String type here. It is ok.
            System.out.println("DataType: " + nameAttrAS.getType().getESObject());

            ExpressionInOCL expr = ocl.createQuery(personAS, "self.name = ''");
            System.out.println("Expression: " + expr);

            // But I get EDataType::Boolean here. I think it is a bug.
            System.out.println("Type: " + expr.getType().getESObject());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static URI createFileURI(String relativePath)
    {
        return URI.createFileURI(new File(relativePath).getAbsolutePath());
    }

}
